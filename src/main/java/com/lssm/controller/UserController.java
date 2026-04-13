package com.lssm.controller;

import com.lssm.dto.LoginDTO;
import com.lssm.service.UserService;
import com.lssm.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import com.lssm.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@Tag(name = "用户接口", description = "用户接口")
@RequestMapping("/user")
public class UserController {

    @Value("${lssm.token.prefix}")
    private String tokenPrefix;

    @Resource()
    private UserService userService;

    @Operation(summary="你好世界")
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @Operation(summary="用户注册")
    @PostMapping("/register")
    public Result<String> register(@RequestBody @Valid LoginDTO userDTO){
        return userService.register(userDTO);
    }

    @Operation(summary="用户登录")
    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody @Valid LoginDTO userDTO){
        return userService.login(userDTO);
    }
    /**
     * 退出登录
     */
    @Operation(summary="用户退出登录")
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest  request){
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith(tokenPrefix)){
            token = token.substring(tokenPrefix.length());
        }
        return userService.logout(token);
    }
    /**
     * 获取用户信息
     */
    @Operation(summary="获取用户信息")
    @GetMapping("/info")
    public Result<User> info(HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return Result.success(user);
    }
}
