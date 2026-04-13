package com.lssm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 内容ID */
    private Long contentId;
    
    /** 评论用户ID */
    private Long userId;
    
    /** 评论内容 */
    private String content;
    
    /** 父评论ID，0为顶级评论 */
    private Long parentId;
    
    /** 序列号（用于分片） */
    private Long sequenceNo;
    
    /** 回复数量 */
    private Integer replyCount;
    
    /** 点赞数量 */
    private Integer likeCount;
    
    /** 创建时间 */
    private LocalDateTime createTime;
    
    /** 更新时间 */
    private LocalDateTime updateTime;
    
    /** 是否删除 */
    private Integer deleted;
}
