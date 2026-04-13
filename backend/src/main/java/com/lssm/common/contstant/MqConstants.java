package com.lssm.common.contstant;

/**
 * MQ 队列常量
 */
public class MqConstants {
    
    // ========== ES 同步相关 ==========
    /** ES 同步交换机 */
    public static final String ES_SYNC_EXCHANGE = "lssm.es.sync.exchange";
    
    /** ES 内容同步队列 */
    public static final String ES_CONTENT_SYNC_QUEUE = "lssm.es.content.sync.queue";
    
    /** ES 内容同步路由键 */
    public static final String ES_CONTENT_SYNC_ROUTING_KEY = "es.content.sync";
    
    /** ES 内容删除队列 */
    public static final String ES_CONTENT_DELETE_QUEUE = "lssm.es.content.delete.queue";
    
    /** ES 内容删除路由键 */
    public static final String ES_CONTENT_DELETE_ROUTING_KEY = "es.content.delete";
    
    // ========== 评论相关 ==========
    /** 评论交换机 */
    public static final String COMMENT_EXCHANGE = "lssm.comment.exchange";
    
    /** 评论落库队列 */
    public static final String COMMENT_PERSIST_QUEUE = "lssm.comment.persist.queue";
    
    /** 评论落库路由键 */
    public static final String COMMENT_PERSIST_ROUTING_KEY = "comment.persist";
    
    /** 评论死信队列 */
    public static final String COMMENT_DLQ = "lssm.comment.dlq";
    
    // ========== 限流相关 ==========
    /** 限流计数器 Key 前缀 */
    public static final String RATE_LIMIT_KEY_PREFIX = "lssm:ratelimit:";
}
