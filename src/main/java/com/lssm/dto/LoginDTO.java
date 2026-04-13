package com.lssm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 登录数据传输对象
 */
@Data
public class LoginDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Length(max = 16, message = "用户名太长了")
    private String nickname;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Length(max = 16,min = 6, message = "密码长度应在6~15")
    private String password;

    /**
     * 记住我
     */
//    private Boolean rememberMe = false;
}
