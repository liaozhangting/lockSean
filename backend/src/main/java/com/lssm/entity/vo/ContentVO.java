package com.lssm.entity.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 内容详情VO
 */
@Data
@Schema(description = "内容详情")
public class ContentVO implements Serializable {

    @Schema(description = "内容ID")
    private Long id;

    @Schema(description = "作者ID")
    private Long authorId;

    @Schema(description = "作者名称")
    private String authorName;

    @Schema(description = "作者头像")
    private String authorAvatar;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "图片列表")
    private List<String> images;

    @Schema(description = "点赞数")
    private Integer likes;

    @Schema(description = "评论数")
    private Integer comments;

    @Schema(description = "分享数")
    private Integer shares;

    @Schema(description = "浏览数")
    private Integer views;

    @Schema(description = "标签列表")
    private List<String> tags;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "是否已点赞")
    private Boolean hasLiked;

    @Schema(description = "热度评分")
    private Double hotScore;
}
