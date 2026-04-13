package com.lssm.utils;


import com.lssm.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;

@Slf4j
@Component
public class JWUtil {
    //密钥
    @Value("${lssm.jwt.secret}")
    private String secret;

    //过期时间
    @Value("${lssm.jwt.expiration}")
    private Long expiration;

    @Value("${lssm.token.prefix}")
    private String tokenPrefix;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey(){
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    /**
     * 生成token
     */
    public String generateToken(User user){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",user.getId());
        claims.put("nickname",user.getNickname());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getNickname())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSigningKey())
                .compact();

    }
    /**
     * 从token中获取claims
     */
    public Claims getClaimsFromToken(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch(Exception e){
            return null;
        }
    }
    /**
     * 验证token
     */
    public boolean validateToken(String token){
        if(token == null || token.isEmpty()){
            log.warn("token为空");
            return false;
        }
        try{
            //1.从token中获取claims
            Claims claims = getClaimsFromToken(token);
            Long userId = claims.get("userId",Long.class);
            //构造Redis key
            String key = tokenPrefix +"uid:"+ userId;
            log.info("查找 Redis key:{}",key);
            //检查redis中是否存在
            if(!redisTemplate.hasKey(key)){
                log.warn("Redis 中不存在 key:{}",key);
                return false;
            }
            //4.验证token
            String redisToken = (String) redisTemplate.opsForValue().get(key);
            if(!redisToken.equals(token)){
                log.warn("token验证失败{}", token);
                return false;
            }
            log.info("token验证成功{},userId:{}", token, userId);
            return true;
            }catch(Exception e){
            log.error("Token已过期",e);
            return false;
        }


    }
    /**
     * 从token中获取用户对象
     */
    public User getUser(String token){
        try {
            Claims claims = getClaimsFromToken(token);
            if(claims == null){
                log.warn("token解析失败");
                return null;
            }

            Long userId = claims.get("userId", Long.class);
            String redisKey = tokenPrefix + "uid:" + userId;

            // 从 Redis 获取用户信息（如果存储了的话）
            // 或者直接从 claims 中构造 User 对象
            User user = new User();
            user.setId(userId);
            user.setNickname(claims.getSubject());
            log.info("从 Redis 中获取用户信息成功,userId:{},nickname:{}", userId, user.getNickname());

            return user;
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return null;
        }
    }
    /**
     * 保存 token
     * @param userId 用户ID
     * @param user 用户
     * @return token
     */
    public String saveToken(Long userId, User user){
        String token = generateToken(user);
        String key = tokenPrefix + userId;

        // 存储 token 用户信息
        redisTemplate.opsForValue().set(key,user,expiration, TimeUnit.SECONDS);

        //存储userId token的映射
        redisTemplate.opsForValue().set(tokenPrefix+"uid:"+userId,token,expiration,TimeUnit.SECONDS);

        return token;

    }
    /**
     * 删除token
     */
    public void removeToken(String token){
        if(token != null && !token.isEmpty()){
            try {
                // 从 token 中获取 userId
                Claims claims = getClaimsFromToken(token);
                if(claims != null){
                    Long userId = claims.get("userId", Long.class);
                    // 删除 uid -> token 的映射
                    String uidKey = tokenPrefix + "uid:" + userId;
                    redisTemplate.delete(uidKey);
                    // 删除 token -> user 的映射
                    String tokenKey = tokenPrefix + userId;
                    redisTemplate.delete(tokenKey);
                    log.info("Token 删除成功, userId: {}", userId);
                }
            } catch (Exception e) {
                log.error("删除 Token 失败", e);
            }
        }
    }
    /**
     * 刷新token
     */
    public void refreshToken(String token){
        try {
            // 解析 token 获取 userId
            Claims claims = getClaimsFromToken(token);
            if(claims == null){
                log.warn("token解析失败");
                return;
            }

            Long userId = claims.get("userId", Long.class);
            refreshTokenById(userId);
        } catch (Exception e) {
            log.error("刷新 token 失败", e);
        }
    }
    public void refreshTokenById(Long userId){
        try{
            String userKey = tokenPrefix + "uid:" + userId;
            if(redisTemplate.hasKey(userKey)){
                redisTemplate.expire(userKey,expiration,TimeUnit.SECONDS);
                log.info("刷新token成功,userId:{}",userId);
            }

        }catch(Exception e){
            log.error("刷新token失败,userid:{}",userId,e);
        }
    }
}
