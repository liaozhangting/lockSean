package com.lssm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lssm.common.contstant.RS;
import com.lssm.config.BCrypt;
import com.lssm.dto.LoginDTO;
import com.lssm.entity.User;
import com.lssm.mapper.UserMapper;
import com.lssm.service.UserService;
import com.lssm.utils.JWUtil;
import com.lssm.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCrypt bCrypt;

    @Autowired
    private JWUtil jwUtil;

    @Override
    public Result<String> register(LoginDTO userDTO) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class).eq(User::getNickname, userDTO.getNickname())
        );
        if(user != null){
            return Result.error(RS.USERNAME_NO_EXIST.status(), RS.USERNAME_NO_EXIST.message());
        }
        user = new User();
        BeanUtils.copyProperties(userDTO, user);
        String encodePassword = bCrypt.passwordEncoder().encode(user.getPassword());
        user.setPassword(encodePassword);
        log.info("用户注册成功"+user.getNickname()+user.getPassword());
        log.info("用户密码加密成功"+encodePassword);
        userMapper.insert(user);
        return Result.success();
    }

    @Override
    public Result<Map<String,Object>> login(LoginDTO userDTO) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class).eq(User::getNickname, userDTO.getNickname())
        );
        if(user == null){
            return Result.error(RS.USERNAME_ERROR.status(), RS.USERNAME_ERROR.message());
        }
        boolean matches = bCrypt.passwordEncoder().matches(userDTO.getPassword(), user.getPassword());
        if(!matches){
            return Result.error(RS.PASSWORD_ERROR.status(), RS.PASSWORD_ERROR.message());
        }
        String token = jwUtil.saveToken(user.getId(), user);
        log.info(user.getNickname()+"用户登录成功"+ token);
        //返回 token和用户信息
        Map<String,Object> result = new HashMap<>();
        result.put("token",token);
        result.put("userInfo",getUserVO(user));
        return Result.success(result);
    }
    @Override
    public Result<String> logout(String token){
        jwUtil.removeToken(token);
        log.info("用户退出登录");
        return Result.success("操作成功");
    }
    private Map<String,Object> getUserVO(User user){
        Map<String,Object> userVO = new HashMap<>();
        userVO.put("id",user.getId());
        userVO.put("nickname",user.getNickname());
        userVO.put("icon",user.getIcon());
        return userVO;
    }

}
