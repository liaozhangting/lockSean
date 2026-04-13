package com.lssm.mq;

import com.lssm.common.contstant.MqConstants;
import com.lssm.service.ContentSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ES 同步消息消费者
 */
@Slf4j
@Component
public class EsSyncConsumer {

    @Autowired
    private ContentSearchService contentSearchService;

    /**
     * 监听内容同步消息
     */
    @RabbitListener(queues = MqConstants.ES_CONTENT_SYNC_QUEUE)
    public void handleContentSync(Long contentId) {
        log.info("收到内容同步消息 contentId={}", contentId);
        try {
            contentSearchService.syncToEs(contentId);
        } catch (Exception e) {
            log.error("内容同步ES失败 contentId={}", contentId, e);
            // 消息会被自动确认或重试，取决于配置
        }
    }

    /**
     * 监听内容删除消息
     */
    @RabbitListener(queues = MqConstants.ES_CONTENT_DELETE_QUEUE)
    public void handleContentDelete(Long contentId) {
        log.info("收到内容删除消息 contentId={}", contentId);
        try {
            contentSearchService.deleteFromEs(contentId);
        } catch (Exception e) {
            log.error("内容从ES删除失败 contentId={}", contentId, e);
        }
    }
}
