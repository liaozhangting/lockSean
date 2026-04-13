package com.lssm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserContentDTO {
    @NotBlank(message = "用户名不能为空")
    private String nickname;
    @NotBlank(message = "用户id不能为空")
    private long userId;
    @NotBlank(message = "用户头像不能为空")
    private String icon;
}
