package com.lssm.common.contstant;

/**
 * 评论分片常量
 */
public class CommentConstants {
    
    /** Redis Key 前缀 */
    public static final String COMMENT_SEQUENCE_KEY = "lssm:comment:sequence:";
    
    /** 评论分片元数据 ZSet Key */
    public static final String COMMENT_SHARD_META_KEY = "lssm:comment:shard:meta:";
    
    /** 评论热点缓存 Key 前缀 */
    public static final String COMMENT_HOT_CACHE_KEY = "lssm:comment:hot:";
    
    /** 评论列表缓存 Key 前缀 */
    public static final String COMMENT_LIST_CACHE_KEY = "lssm:comment:list:";
    
    /** 热点识别阈值（评论增速，每秒） */
    public static final int HOT_THRESHOLD = 10;
    
    /** 分片大小 */
    public static final int SHARD_SIZE = 100;
    
    /** 热点预热缓存时间（秒） */
    public static final int HOT_CACHE_TTL = 300;
    
    /** 评论列表缓存时间（秒） */
    public static final int COMMENT_CACHE_TTL = 60;
    
    /** 序列号断点扫描间隔（毫秒） */
    public static final long SEQUENCE_SCAN_INTERVAL = 60000;
}
