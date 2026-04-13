package com.lssm.controller;

import com.lssm.common.annotation.RateLimit;
import com.lssm.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 测试限流功能的控制器
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Tag(name = "测试接口", description = "用于测试限流等功能")
public class TestController {

    @RateLimit(key = "hello", maxCount = 10, window = 60, message = "你好接口请求过于频繁")
    @Operation(summary = "测试限流 - 你好接口")
    @GetMapping("/hello")
    public Result<String> hello() {
        log.info("hello 接口被调用");
        return Result.success("Hello, World!");
    }

    @RateLimit(key = "publish", maxCount = 5, window = 60, message = "发布内容过于频繁，请稍后再试")
    @Operation(summary = "测试限流 - 发布接口")
    @PostMapping("/publish")
    public Result<String> publish(
            @Parameter(description = "内容") @RequestParam String content) {
        log.info("publish 接口被调用, content={}", content);
        return Result.success("发布成功!");
    }

    @RateLimit(maxCount = 100, window = 60)
    @Operation(summary = "测试限流 - 默认key（方法名）")
    @GetMapping("/default")
    public Result<String> defaultLimit() {
        log.info("defaultLimit 接口被调用");
        return Result.success("默认限流测试成功!");
    }
}
