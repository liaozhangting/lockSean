package com.lssm.common.annotation;

import java.lang.annotation.*;

/**
 * 限流注解
 * 结合 AOP 切面实现接口限流防刷
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    
    /**
     * 限流 key
     */
    String key() default "";
    
    /**
     * 时间窗口（秒）
     */
    int window() default 60;
    
    /**
     * 最大请求次数
     */
    int maxCount() default 100;
    
    /**
     * 限流提示消息
     */
    String message() default "请求过于频繁，请稍后再试";
}
