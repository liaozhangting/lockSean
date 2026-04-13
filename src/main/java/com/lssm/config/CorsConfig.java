package com.lssm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 1. 创建 CORS 配置对象
        CorsConfiguration config = new CorsConfiguration();
        // 允许前端域名访问（* 表示允许所有域名，生产环境建议指定具体域名，如 http://localhost:5173）
        config.addAllowedOrigin("http://localhost:5173");
        // 允许跨域请求携带 cookie
        config.setAllowCredentials(true);
        // 允许的请求方法（GET、POST、PUT、DELETE 等）
        config.addAllowedMethod("*");
        // 允许的请求头
        config.addAllowedHeader("*");
        // 预检请求的有效期（单位：秒），避免频繁预检
        config.setMaxAge(3600L);

        // 2. 配置路径映射（/** 表示所有路径都启用 CORS）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        // 3. 返回 CORS 过滤器
        return new CorsFilter(source);
    }
}
