package com.lssm.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lssm.common.context.BaseContext;
import com.lssm.dto.ContentDTO;
import com.lssm.dto.PageCursorDTO;
import com.lssm.dto.UserDTO;
import com.lssm.entity.Content;
import com.lssm.entity.User;
import com.lssm.entity.vo.ContentVO;
import com.lssm.mapper.ContentMapper;
import com.lssm.mq.EsSyncProducer;
import com.lssm.service.ContentService;
import com.lssm.utils.Result;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content> implements ContentService {
    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private EsSyncProducer esSyncProducer;
    //L1缓存，本地缓存(Caffeine)
    private Cache<Long,ContentVO> localCache;

    //布隆过滤器
    private RBloomFilter<Long> bloomFilter;

    // 缓存Key前缀
    private static final String CONTENT_DETAIL_KEY = "content:detail:";
    private static final String CONTENT_COUNTER_KEY = "content:counter:";
    private static final String CONTENT_HOT_RANK_KEY = "content:hot:rank";
    private static final String CONTENT_LOCK_KEY = "content:lock:";
    private static final String BLOOM_FILTER_KEY = "content:bloom";

    // 缓存过期时间
    private static final long DETAIL_EXPIRE = 30; // 30分钟
    private static final long HOT_RANK_EXPIRE = 5; // 5分钟

    @PostConstruct
    public void init() {
        //初始化本地缓存
        localCache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(HOT_RANK_EXPIRE, TimeUnit.MINUTES)
                .recordStats()
                .build();

        //初始化布隆过滤器
        bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_KEY);
        if (!bloomFilter.isExists()) {
            bloomFilter.tryInit(10000000L, 0.01);
        }


    }
    /**
     * 发布内容
     * @param contentDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> publish(ContentDTO contentDTO) {
        //1.保存到数据库
        log.info("用户发布内容"+contentDTO.getContent());
        UserDTO userDTO = BaseContext.getUserDTO();
        Content content = new Content()
                .setAuthorId(userDTO.getId())
                .setTitle(contentDTO.getTitle())
                .setContent(contentDTO.getContent())
                .setImages(contentDTO.getImages())
                .setTags(contentDTO.getTags());
        contentMapper.insert(content);
        long contentId = content.getId();

        //2.添加布隆过滤器
        bloomFilter.add(contentId);

        //3.初始化Redis计数器
        initXCounters(contentId);

        //4.异步同步到ES（使用消息队列解耦）
        esSyncProducer.sendContentSync(contentId);
        log.info("用户ID: {}, 发布内容成功, contentId: {}", userDTO.getId(), contentId);

        return Result.success();

    }


    /**
     * 初始化计数器
     * @param contentId
     */
    private void initXCounters(long contentId) {
        String key = CONTENT_COUNTER_KEY + contentId;
        redisTemplate.opsForHash().put(key, "likes", 0);
        redisTemplate.opsForHash().put(key, "comments", 0);
        redisTemplate.opsForHash().put(key, "shares", 0);
        redisTemplate.opsForHash().put(key, "views", 0);

    }
    /**
     * 获取内容详情
     * @param contentId
     * @return
     */
    @Override
    public Result<ContentVO> get(long contentId) {
        //0,参数校验
        if(contentId <= 0){
            return Result.error("参数错误");
        }
        //1.布隆过滤器（防止缓存穿透)
        if(!bloomFilter.contains(contentId)){
            return Result.error("内容不存在");
        }
        //2.L1缓存,本地缓存查询
        ContentVO contentVO = localCache.getIfPresent(contentId);
        if(contentVO != null){
            log.info("本地缓存命中，contentId={}",contentId);
            asyncIncrementViews(contentId);
            return Result.success(contentVO);
        }
        //3.L2缓存,Redis缓存查询
        contentVO = getFromRedis(contentId);
        if(contentVO != null){
            log.info("Redis缓存命中，contentId={}",contentId);
            localCache.put(contentId,contentVO);
            asyncIncrementViews(contentId);
            return Result.success(contentVO);
        }
        //分布式锁防止缓存击穿
        RLock lock = redissonClient.getLock(CONTENT_LOCK_KEY+contentId);
        try{
            //尝试获取锁，最多等待5秒，锁10秒后自动释放
            boolean locked = lock.tryLock(5,10,TimeUnit.SECONDS);
            if(!locked){
                return Result.error("系统繁忙，请稍后重试");
            }
            //双重检查；再次查询Redis（可能其他线程已经加载
            contentVO = getFromRedis((contentId));
            if(contentVO != null){
                log.info("Redis缓存命中，contentId={}",contentId);
                localCache.put(contentId,contentVO);
                asyncIncrementViews(contentId);
                return Result.success(contentVO);
            }
            //L3缓存：数据库查询
            Content  content = this.getById(contentId);
            if(content == null){
                //缓存空对象，防止缓存穿透
                cacheNullValue(contentId);
                return Result.error("内容不存在");
            }
            //6.构建VO并设置计数器
            contentVO = builderContentVO(content);

            //7.回填缓存
            cacheToRedis(contentId,contentVO);
            localCache.put(contentId,contentVO);

            asyncIncrementViews(contentId);
            log.info("数据库查询成功，contentId={}",contentId);
            return Result.success(contentVO);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            log.error("获取锁异常",e);
            return  Result.error("系统繁忙，请稍后重试");
        }finally {
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }

    @Override
    public Result<List<Content>> getRecommendContents(PageCursorDTO pageCursor) {
        if (pageCursor.getPageSize() == null || pageCursor.getPageSize() <= 0) {
            pageCursor.setPageSize(10);
        }
        if (pageCursor.getPageSize() > 100) {
            pageCursor.setPageSize(100);
        }
        
        Long lastId = pageCursor.getLastId();
        if (lastId == null || lastId <= 0) {
            lastId = Long.MAX_VALUE;
        }
        
        // 使用游标分页查询
        Page<Content> page = new Page<>(1, pageCursor.getPageSize()); // page参数不影响，只是占位
        Page<Content> result = contentMapper.selectHomeRecommendCursor(lastId, pageCursor.getPageSize());
        
        log.info("游标分页查询首页推荐 lastId={}, pageSize={}, resultSize={}", 
                lastId, pageCursor.getPageSize(), result.getRecords().size());
        
        return Result.success(result.getRecords());
    }

    @Override
    public Result<List<ContentVO>> getFollowingContent(Integer page, Integer pageSize) {
        return null;
    }
    
    /**
     * 获取用户内容列表（游标分页）
     */
    @Override
    public Result<List<Content>> getUserContents(PageCursorDTO pageCursor) {
        if (pageCursor.getPageSize() == null || pageCursor.getPageSize() <= 0) {
            pageCursor.setPageSize(10);
        }
        if (pageCursor.getPageSize() > 100) {
            pageCursor.setPageSize(100);
        }
        
        Long lastId = pageCursor.getLastId();
        if (lastId == null || lastId <= 0) {
            lastId = Long.MAX_VALUE;
        }
        
        Page<Content> result = contentMapper.selectCursorPageWithFilters(
                lastId,
                pageCursor.getUserId(),
                pageCursor.getType(),
                pageCursor.getPageSize()
        );
        
        log.info("游标分页查询用户内容 lastId={}, userId={}, pageSize={}, resultSize={}", 
                lastId, pageCursor.getUserId(), pageCursor.getPageSize(), result.getRecords().size());
        
        return Result.success(result.getRecords());
    }

    private void asyncPersistCounter(Long contentId, String field, int delta) {
        // TODO: 发送到消息队列，批量处理
        // 示例：rabbitTemplate.convertAndSend("content.counter", message);
    }
    /**
     * 从Redis缓存中获取内容详情
     * @param contentId
     * @return
     */
    private ContentVO getFromRedis(long contentId) {
        String key = CONTENT_DETAIL_KEY + contentId;
        Object obj = redisTemplate.opsForValue().get(key);
        if("NULL".equals(String.valueOf(obj))){
            return null;
        }
        if(obj != null){
            return (ContentVO) obj;
        }
        return null;
    }
    /**
     * 异步增加浏览量
     */
    private void asyncIncrementViews(Long contentId) {
        //TODO: 添加异步处理
        // 使用异步线程池执行
        String counterKey = CONTENT_COUNTER_KEY + contentId;
        redisTemplate.opsForHash().increment(counterKey, "views", 1);
        asyncPersistCounter(contentId, "views", 1);
    }
    /**
     * 缓存空对象
     * @param contentId
     */
    private void cacheNullValue(long contentId) {
        String key = CONTENT_DETAIL_KEY + contentId;
        redisTemplate.opsForValue().set(key, "NULL", DETAIL_EXPIRE, TimeUnit.MINUTES);
    }
    /**
     * 构建内容详情VO
     * @param content
     * @return
     */
    private ContentVO builderContentVO(Content content) {
        ContentVO vo = new ContentVO();
        BeanUtils.copyProperties(content, vo);
        //从Redis中获取实时计数
        String counterKey = CONTENT_COUNTER_KEY + content.getId();
        vo.setLikes(getCounter(counterKey, "likes"));
        vo.setComments(getCounter(counterKey, "comments"));
        vo.setShares(getCounter(counterKey, "shares"));
        vo.setViews(getCounter(counterKey, "views"));

        return vo;
    }
    /**
     * 获取Redis计数器
     * @param key
     * @param field
     * @return
     */
    private int getCounter(String key, String field){
        Object value = redisTemplate.opsForHash().get(key, field);
        return value != null ? Integer.parseInt(value.toString()) : 0;
    }
    /**
     * 缓存内容详情到Redis
     * @param contentId
     * @param contentVO
     */
    private void cacheToRedis(long contentId, ContentVO contentVO) {
        String key = CONTENT_DETAIL_KEY + contentId;
        redisTemplate.opsForValue().set(key, contentVO, DETAIL_EXPIRE, TimeUnit.MINUTES);
    }


}
