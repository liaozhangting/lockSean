package com.lssm.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Schema(description = "内容类")
@TableName("tb_content")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Content {
    
    @Schema(description = "内容id")
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @Schema(description = "作者id")
    private Long authorId;
    
    @Schema(description = "标题")
    private String title;
    
    @Schema(description = "内容")
    private String content;
    
    @Schema(description = "图片，多个用逗号分隔")
    private String images;
    
    @Schema(description = "视频URL")
    private String videoUrl;
    
    @Schema(description = "内容类型：image-图文，video-视频")
    private String type;
    
    @Schema(description = "点赞数")
    private Integer likeCount;
    
    @Schema(description = "评论数")
    private Integer commentCount;
    
    @Schema(description = "收藏数")
    private Integer collectCount;
    
    @Schema(description = "分享数")
    private Integer shareCount;
    
    @Schema(description = "浏览数")
    private Integer viewCount;
    
    @Schema(description = "纬度")
    private Double latitude;
    
    @Schema(description = "经度")
    private Double longitude;
    
    @Schema(description = "位置")
    private String location;
    
    @Schema(description = "标签")
    private String tags;
    
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
