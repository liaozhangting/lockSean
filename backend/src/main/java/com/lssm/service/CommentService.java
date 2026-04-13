package com.lssm.service;

import com.lssm.dto.CommentDTO;
import com.lssm.entity.Comment;
import com.lssm.utils.Result;
import java.util.List;

public interface CommentService {
    
    /**
     * 发布评论
     */
    Result<String> publish(CommentDTO commentDTO);
    
    /**
     * 获取评论列表（分片查询）
     */
    Result<List<Comment>> getComments(Long contentId, Long lastSequenceNo, Integer pageSize);
    
    /**
     * 获取评论详情
     */
    Result<Comment> getComment(Long commentId);
    
    /**
     * 删除评论
     */
    Result<String> delete(Long commentId, Long userId);
}
