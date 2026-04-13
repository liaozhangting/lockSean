package com.lssm.common.aspect;

import com.lssm.common.annotation.RateLimit;
import com.lssm.common.contstant.MqConstants;
import com.lssm.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;

/**
 * 限流 AOP 切面
 * 使用 Redis 计数器实现接口限流
 */
@Slf4j
@Aspect
@Component
public class RateLimitAspect {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 限流 Lua 脚本，保证原子性
     */
    private static final String RATE_LIMIT_SCRIPT = 
            "local key = KEYS[1] " +
            "local limit = tonumber(ARGV[1]) " +
            "local current = tonumber(redis.call('get', key) or '0') " +
            "if current + 1 > limit then " +
            "    return 0 " +
            "else " +
            "    redis.call('incr', key) " +
            "    if current == 0 then " +
            "        redis.call('expire', key, ARGV[2]) " +
            "    end " +
            "    return 1 " +
            "end";

    @Around("@annotation(com.lssm.common.annotation.RateLimit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 1. 获取限流注解配置
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        
        // 2. 构建限流 Key
        HttpServletRequest request = getRequest();
        String clientId = getClientId(request);
        String key = buildKey(rateLimit.key(), method.getName(), clientId);
        
        // 3. 执行限流检查
        long window = rateLimit.window();
        long maxCount = rateLimit.maxCount();
        
        Boolean allowed = checkRateLimit(key, maxCount, window);
        
        if (allowed == null || !allowed) {
            log.warn("限流触发 key={}, maxCount={}, window={}s", key, maxCount, window);
            return Result.error(rateLimit.message());
        }
        
        log.debug("限流检查通过 key={}", key);
        return point.proceed();
    }

    /**
     * 执行限流检查
     */
    private Boolean checkRateLimit(String key, long maxCount, long window) {
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>(RATE_LIMIT_SCRIPT, Long.class);
            Long result = redisTemplate.execute(
                    script,
                    Collections.singletonList(key),
                    String.valueOf(maxCount),
                    String.valueOf(window)
            );
            return result != null && result == 1L;
        } catch (Exception e) {
            log.error("限流检查异常 key={}", key, e);
            // 限流异常时默认通过，避免影响业务
            return true;
        }
    }

    /**
     * 构建限流 Key
     */
    private String buildKey(String annotationKey, String methodName, String clientId) {
        if (annotationKey != null && !annotationKey.isEmpty()) {
            return MqConstants.RATE_LIMIT_KEY_PREFIX + annotationKey + ":" + clientId;
        }
        return MqConstants.RATE_LIMIT_KEY_PREFIX + methodName + ":" + clientId;
    }

    /**
     * 获取客户端标识
     */
    private String getClientId(HttpServletRequest request) {
        // 优先使用用户ID（登录后）
        Object userId = request.getAttribute("userId");
        if (userId != null) {
            return "user:" + userId;
        }
        
        // 使用 IP 地址
        String ip = getClientIp(request);
        return "ip:" + ip;
    }

    /**
     * 获取客户端 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多级代理时取第一个 IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 获取 HttpServletRequest
     */
    private HttpServletRequest getRequest() {
        return ((org.springframework.web.context.request.ServletRequestAttributes)
                org.springframework.web.context.request.RequestContextHolder
                        .getRequestAttributes()).getRequest();
    }
}
