package com.lssm.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * CREATE TABLE tb_content(
 *     id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
 *     author_id BIGINT NOT NULL COMMENT '创作者id',
 *     title VARCHAR(255) DEFAULT NULL COMMENT '标题',
 *     images VARCHAR(2048) NOT NULL COMMENT '内容图片，多张以,隔开',
 *     content TEXT NOT NULL COMMENT '内容',
 *     likes INT DEFAULT 0 COMMENT '点赞数量',
 *     comments INT DEFAULT 0 COMMENT '评论数量',
 *     shares INT DEFAULT 0 COMMENT '分享数量',
 *     tags VARCHAR(255) DEFAULT '' COMMENT '标签，多个以,隔开',
 *     views BIGINT DEFAULT 0 COMMENT '浏览数量',
 *     create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 *     update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
 * )
 */
@Data
public class ContentDTO {

    @NotBlank(message = "标题不能为空")
    @Length(max = 255, message = "标题太长了")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Length(max = 2048, message = "内容太长了")
    private String content;

    @Length(max = 2048, message = "图片/视频超出上限了")
    private String images;

    @Length(max = 255, message = "标签太长了")
    private String tags;
}
