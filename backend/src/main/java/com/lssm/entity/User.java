package com.lssm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Schema(description = "用户信息")
@TableName("tb_user")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class User {
    @Schema(description = "用户id")
    private long id;
    @Schema(description = "昵称")
    @TableField("nick_name")
    private String nickname;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "头像")
    private String icon;
//    @Schema(description ="创建时间")
//    private String createTime;
//    @Schema(description = "更新时间")
//    private String updateTime;

}
