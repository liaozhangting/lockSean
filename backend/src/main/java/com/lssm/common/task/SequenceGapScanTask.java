package com.lssm.common.task;

import com.lssm.common.contstant.CommentConstants;
import com.lssm.entity.Comment;
import com.lssm.mapper.CommentMapper;
import com.lssm.utils.CommentShardUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 定时任务：扫描序列号断层，作为缓存与数据库一致性的兜底机制
 */
@Slf4j
@Component
public class SequenceGapScanTask {

    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private CommentShardUtils commentShardUtils;

    /**
     * 定期扫描评论序列号断层
     * 默认每分钟执行一次
     */
    @Scheduled(fixedRate = CommentConstants.SEQUENCE_SCAN_INTERVAL)
    public void scanSequenceGap() {
        log.info("开始扫描评论序列号断层...");
        
        try {
            // 获取所有评论序列号相关的 Redis Key
            Set<String> keys = redisTemplate.keys(CommentConstants.COMMENT_SEQUENCE_KEY + "*");
            if (keys == null || keys.isEmpty()) {
                log.info("没有发现评论序列号 Key");
                return;
            }
            
            int scanCount = 0;
            int gapCount = 0;
            
            for (String key : keys) {
                // 提取 contentId
                String contentIdStr = key.replace(CommentConstants.COMMENT_SEQUENCE_KEY, "");
                if (contentIdStr.contains(":rate")) {
                    // 这是评论增速计数 Key，跳过
                    continue;
                }
                
                Long contentId = Long.parseLong(contentIdStr);
                Long redisSeq = commentShardUtils.getCurrentSequenceNo(contentId);
                
                if (redisSeq == null || redisSeq <= 0) {
                    continue;
                }
                
                // 查询数据库最大序列号
                Long dbMaxSeq = commentMapper.selectMaxSequenceNo(contentId);
                if (dbMaxSeq == null) {
                    dbMaxSeq = 0L;
                }
                
                // 检查断层
                if (redisSeq > dbMaxSeq + 1) {
                    log.warn("发现序列号断层 contentId={}, dbMaxSeq={}, redisSeq={}", 
                            contentId, dbMaxSeq, redisSeq);
                    gapCount++;
                    
                    // 触发补录（这里可以发送 MQ 消息异步处理）
                    triggerGapFill(contentId, dbMaxSeq, redisSeq);
                }
                
                scanCount++;
            }
            
            log.info("序列号断层扫描完成，扫描 {} 个内容，发现 {} 个断层", scanCount, gapCount);
            
        } catch (Exception e) {
            log.error("序列号断层扫描异常", e);
        }
    }
    
    /**
     * 触发断层补录
     */
    private void triggerGapFill(Long contentId, Long fromSeq, Long toSeq) {
        log.info("触发断层补录 contentId={}, fromSeq={}, toSeq={}", contentId, fromSeq, toSeq);
        // 实际生产中应该：
        // 1. 从 Redis 缓存获取缺失的评论数据
        // 2. 批量写入数据库
        // 3. 更新分片元数据
    }
}
