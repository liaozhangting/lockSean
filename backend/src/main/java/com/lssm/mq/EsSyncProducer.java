package com.lssm.mq;

import com.lssm.common.contstant.MqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ES 同步消息生产者
 */
@Slf4j
@Component
public class EsSyncProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送内容同步消息
     */
    public void sendContentSync(Long contentId) {
        log.info("发送内容同步消息 contentId={}", contentId);
        rabbitTemplate.convertAndSend(
                MqConstants.ES_SYNC_EXCHANGE,
                MqConstants.ES_CONTENT_SYNC_ROUTING_KEY,
                contentId
        );
    }

    /**
     * 发送内容删除消息
     */
    public void sendContentDelete(Long contentId) {
        log.info("发送内容删除消息 contentId={}", contentId);
        rabbitTemplate.convertAndSend(
                MqConstants.ES_SYNC_EXCHANGE,
                MqConstants.ES_CONTENT_DELETE_ROUTING_KEY,
                contentId
        );
    }
}
