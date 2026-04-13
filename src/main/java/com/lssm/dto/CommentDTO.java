package com.lssm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDTO {
    
    @NotNull(message = "内容ID不能为空")
    private Long contentId;
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotBlank(message = "评论内容不能为空")
    private String content;
    
    /** 父评论ID，0为顶级评论 */
    private Long parentId = 0L;
}
