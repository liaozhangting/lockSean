package com.lssm.utils;

import com.lssm.common.contstant.CommentConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CommentShardUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 生成评论序列号（Redis INCR）
     */
    public Long generateSequenceNo(Long contentId) {
        String key = CommentConstants.COMMENT_SEQUENCE_KEY + contentId;
        Long sequenceNo = redisTemplate.opsForValue().increment(key);
        if (sequenceNo != null && sequenceNo == 1L) {
            // 首次生成，设置过期时间
            redisTemplate.expire(key, 7, TimeUnit.DAYS);
        }
        log.info("生成评论序列号 contentId={}, sequenceNo={}", contentId, sequenceNo);
        return sequenceNo;
    }

    /**
     * 获取当前序列号
     */
    public Long getCurrentSequenceNo(Long contentId) {
        String key = CommentConstants.COMMENT_SEQUENCE_KEY + contentId;
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value.toString()) : 0L;
    }

    /**
     * 更新分片元数据（ZSet：分片起始序列号为 score）
     */
    public void updateShardMeta(Long contentId, Long shardStartSeq, Long shardEndSeq) {
        String key = CommentConstants.COMMENT_SHARD_META_KEY + contentId;
        // ZSet: member = shardEndSeq, score = shardStartSeq
        redisTemplate.opsForZSet().add(key, shardEndSeq.toString(), shardStartSeq.doubleValue());
        log.info("更新分片元数据 contentId={}, shardStartSeq={}, shardEndSeq={}", 
                contentId, shardStartSeq, shardEndSeq);
    }

    /**
     * 获取分片信息
     */
    public ShardInfo getShardInfo(Long contentId, Long sequenceNo) {
        String key = CommentConstants.COMMENT_SHARD_META_KEY + contentId;
        // 找到序列号所在的分片
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        // 按 score 倒序，获取所有分片，找到第一个 score <= sequenceNo 的
        var shards = zSetOps.reverseRangeWithScores(key, 0, -1);
        if (shards == null || shards.isEmpty()) {
            return null;
        }
        for (var shard : shards) {
            Long shardStartSeq = shard.getScore().longValue();
            String shardEndStr = shard.getValue().toString();
            Long shardEndSeq = Long.parseLong(shardEndStr);
            if (shardStartSeq <= sequenceNo) {
                return new ShardInfo(shardStartSeq, shardEndSeq);
            }
        }
        return null;
    }

    /**
     * 计算序列号所属分片
     */
    public Long calculateShardIndex(Long sequenceNo) {
        return sequenceNo / CommentConstants.SHARD_SIZE;
    }

    /**
     * 判断是否为热点内容（评论增速超过阈值）
     */
    public boolean isHotContent(Long contentId) {
        String key = CommentConstants.COMMENT_SEQUENCE_KEY + contentId + ":rate";
        Long currentCount = redisTemplate.opsForValue().increment(key);
        if (currentCount != null && currentCount == 1L) {
            // 1秒后过期，用于计数
            redisTemplate.expire(key, 1, TimeUnit.SECONDS);
        }
        log.info("评论增速检测 contentId={}, count={}, threshold={}", 
                contentId, currentCount, CommentConstants.HOT_THRESHOLD);
        return currentCount != null && currentCount >= CommentConstants.HOT_THRESHOLD;
    }

    /**
     * 缓存热点评论列表
     */
    public void cacheHotComments(Long contentId, Long shardStartSeq, Long shardEndSeq, Object comments) {
        String key = CommentConstants.COMMENT_HOT_CACHE_KEY + contentId + ":" + shardStartSeq;
        redisTemplate.opsForValue().set(key, comments, CommentConstants.HOT_CACHE_TTL, TimeUnit.SECONDS);
        log.info("缓存热点评论 contentId={}, shardStartSeq={}, ttl={}s", 
                contentId, shardStartSeq, CommentConstants.HOT_CACHE_TTL);
    }

    /**
     * 获取热点评论缓存
     */
    public Object getHotCommentsCache(Long contentId, Long shardStartSeq) {
        String key = CommentConstants.COMMENT_HOT_CACHE_KEY + contentId + ":" + shardStartSeq;
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 分片信息内部类
     */
    public static class ShardInfo {
        private final Long shardStartSeq;
        private final Long shardEndSeq;

        public ShardInfo(Long shardStartSeq, Long shardEndSeq) {
            this.shardStartSeq = shardStartSeq;
            this.shardEndSeq = shardEndSeq;
        }

        public Long getShardStartSeq() { return shardStartSeq; }
        public Long getShardEndSeq() { return shardEndSeq; }
    }
}
