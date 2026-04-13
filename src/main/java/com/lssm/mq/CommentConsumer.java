package com.lssm.mq;

import com.lssm.common.contstant.CommentConstants;
import com.lssm.common.contstant.MqConstants;
import com.lssm.entity.Comment;
import com.lssm.mapper.CommentMapper;
import com.lssm.utils.CommentShardUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 评论 MQ 消费者：处理评论落库和死信队列
 */
@Slf4j
@Component
public class CommentConsumer {

    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private CommentShardUtils commentShardUtils;

    /**
     * 处理评论落库消息
     */
    @RabbitListener(queues = MqConstants.COMMENT_PERSIST_QUEUE)
    public void handleCommentPersist(Comment comment) {
        log.info("收到评论落库消息 commentId={}, contentId={}", comment.getId(), comment.getContentId());
        try {
            // 评论已由主路径（同步）写入，这里只做补偿性检查
            // 如果序列号有断层，触发补录
            checkAndFillSequenceGap(comment.getContentId());
        } catch (Exception e) {
            log.error("处理评论落库消息失败 commentId={}", comment.getId(), e);
            throw e; // 抛出异常，消息进入死信队列
        }
    }

    /**
     * 处理死信队列消息
     * 定期扫描死信队列恢复丢失数据
     */
    @RabbitListener(queues = MqConstants.COMMENT_DLQ)
    public void handleDeadLetter(Comment comment) {
        log.warn("收到评论死信消息 commentId={}, contentId={}", comment.getId(), comment.getContentId());
        try {
            // 尝试重新落库
            commentMapper.insert(comment);
            log.info("死信队列评论恢复成功 commentId={}", comment.getId());
        } catch (Exception e) {
            log.error("死信队列评论恢复失败 commentId={}", comment.getId(), e);
            // 可能是重复插入，需要确保幂等
        }
    }

    /**
     * 检查并补录序列号断层
     */
    private void checkAndFillSequenceGap(Long contentId) {
        Long currentSeq = commentShardUtils.getCurrentSequenceNo(contentId);
        if (currentSeq == null || currentSeq <= 0) {
            return;
        }
        
        // 查询数据库中的最大序列号
        var maxSeqInDb = commentMapper.selectMaxSequenceNo(contentId);
        if (maxSeqInDb == null) {
            return;
        }
        
        // 如果有断层，触发补录逻辑
        if (currentSeq > maxSeqInDb + 1) {
            log.warn("检测到评论序列号断层 contentId={}, dbMaxSeq={}, redisSeq={}", 
                    contentId, maxSeqInDb, currentSeq);
            // 发送补录消息（实际生产中应该有补录队列）
            fillSequenceGap(contentId, maxSeqInDb, currentSeq);
        }
    }
    
    /**
     * 补录序列号断层
     */
    private void fillSequenceGap(Long contentId, Long fromSeq, Long toSeq) {
        log.info("开始补录评论序列号断层 contentId={}, fromSeq={}, toSeq={}", contentId, fromSeq, toSeq);
        // 实际生产中应该从 Redis 获取缺失的评论数据并落库
        // 这里只是记录日志
    }
}
