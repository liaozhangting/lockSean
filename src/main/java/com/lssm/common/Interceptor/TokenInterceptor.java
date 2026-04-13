package com.lssm.common.Interceptor;

import com.lssm.common.context.BaseContext;
import com.lssm.dto.UserDTO;
import com.lssm.entity.User;
import com.lssm.utils.JWUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HttpServletBean;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JWUtil jwUtil;

    @Value("${lssm.token.prefix}")
    private String tokenPrefix;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler){
        //1.获取请求头中的token
        String token = request.getHeader("Authorization");
        log.info("收到的Authorization: {}", token);
        log.info("配置的tokenPrefix: {}", tokenPrefix);

        if(token == null || !token.startsWith(tokenPrefix)){
            log.warn("token格式错误或缺失");
            response.setStatus(401);
            return false;
        }
        token = token.substring(tokenPrefix.length()).trim();
        //2.验证token
        if(!jwUtil.validateToken(token)){
            log.warn("token验证失败{}", token);
            response.setStatus(401);
            return false;
        }
        //3.刷新token过期时间
        jwUtil.refreshToken(token);

        //4.获取用户信息并存入TreadLocal
        User user = jwUtil.getUser(token);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        BaseContext.setUserDTO(userDTO);
        request.setAttribute("user", user);
        request.setAttribute("token", token);
        return true;
    }

}
