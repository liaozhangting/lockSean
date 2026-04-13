package com.lssm.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //队列名称
    public static final String COUNTER_QUEUE = "content.counter.queue";
    //交换机名称
    public static final String COUNTER_EXCHANGE = "content.counter.exchange";
    //路由键
    public static final String COUNTER_ROUTING_KEY = "content.counter";

    /**
     * 创建队列
     */
    @Bean
    public Queue counterExchange() {
        return QueueBuilder.durable(COUNTER_QUEUE)
                .build();
    }

    /**
     * 创建直连交换机
     */
    @Bean
    public DirectExchange directExchange() {
        return ExchangeBuilder.directExchange(COUNTER_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding counterBinding(Queue counterQueue, DirectExchange counterExchange){
        return BindingBuilder.bind(counterQueue)
                .to(counterExchange)
                .with(COUNTER_ROUTING_KEY);
    }
}
