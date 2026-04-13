package com.lssm.common.context;

import com.lssm.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseContext {

    private static final Logger log = LoggerFactory.getLogger(BaseContext.class);

    public static ThreadLocal<UserDTO> threadLocal = new ThreadLocal<>();

    public static void setUserDTO(UserDTO userDTO) {
        log.info("设置用户对象: {}, 线程: {}", userDTO.getId(), Thread.currentThread().getName());
        threadLocal.set(userDTO);
    }

    public static UserDTO getUserDTO() {
        UserDTO userDTO = threadLocal.get();
        log.info("获取用户ID: {}, 线程: {}", userDTO.getId(), Thread.currentThread().getName());
        return userDTO;
    }

    public static void removeUserDTO() {
        threadLocal.remove();
    }

}
