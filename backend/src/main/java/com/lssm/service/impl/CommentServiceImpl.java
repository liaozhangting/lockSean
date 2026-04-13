package com.lssm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lssm.common.contstant.CommentConstants;
import com.lssm.dto.CommentDTO;
import com.lssm.entity.Comment;
import com.lssm.mapper.CommentMapper;
import com.lssm.service.CommentService;
import com.lssm.utils.CommentShardUtils;
import com.lssm.utils.CommentShardUtils.ShardInfo;
import com.lssm.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private CommentShardUtils commentShardUtils;

    @Override
    @Transactional
    public Result<String> publish(CommentDTO commentDTO) {
        // 1. 生成序列号
        Long sequenceNo = commentShardUtils.generateSequenceNo(commentDTO.getContentId());
        
        // 2. 构建评论实体
        Comment comment = new Comment();
        comment.setContentId(commentDTO.getContentId());
        comment.setUserId(commentDTO.getUserId());
        comment.setContent(commentDTO.getContent());
        comment.setParentId(commentDTO.getParentId() != null ? commentDTO.getParentId() : 0L);
        comment.setSequenceNo(sequenceNo);
        comment.setReplyCount(0);
        comment.setLikeCount(0);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        comment.setDeleted(0);
        
        // 3. 插入数据库
        commentMapper.insert(comment);
        
        // 4. 更新分片元数据
        Long shardIndex = commentShardUtils.calculateShardIndex(sequenceNo);
        Long shardStartSeq = shardIndex * CommentConstants.SHARD_SIZE + 1;
        Long shardEndSeq = (shardIndex + 1) * CommentConstants.SHARD_SIZE;
        commentShardUtils.updateShardMeta(commentDTO.getContentId(), shardStartSeq, shardEndSeq);
        
        // 5. 检查是否为热点内容，触发预热
        if (commentShardUtils.isHotContent(commentDTO.getContentId())) {
            log.info("内容 {} 已成为热点，开始预热缓存", commentDTO.getContentId());
            // 异步预热可以在这里触发（实际生产中用 @Async）
        }
        
        log.info("评论发布成功 contentId={}, commentId={}, sequenceNo={}", 
                commentDTO.getContentId(), comment.getId(), sequenceNo);
        return Result.success("评论发布成功");
    }

    @Override
    public Result<List<Comment>> getComments(Long contentId, Long lastSequenceNo, Integer pageSize) {
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
        
        List<Comment> comments = new ArrayList<>();
        
        // 1. 如果有缓存，直接返回（热点内容）
        if (lastSequenceNo == null || lastSequenceNo == 0) {
            // 首次查询，检查热点缓存
            Long currentSeq = commentShardUtils.getCurrentSequenceNo(contentId);
            if (currentSeq > 0) {
                Long shardIndex = commentShardUtils.calculateShardIndex(currentSeq);
                Long shardStartSeq = shardIndex * CommentConstants.SHARD_SIZE + 1;
                Object cached = commentShardUtils.getHotCommentsCache(contentId, shardStartSeq);
                if (cached != null) {
                    log.info("命中热点缓存 contentId={}, shardStartSeq={}", contentId, shardStartSeq);
                    // 缓存命中，直接返回（实际需要反序列化）
                }
            }
        }
        
        // 2. 分片查询
        Long startSequenceNo = (lastSequenceNo != null && lastSequenceNo > 0) 
                ? lastSequenceNo + 1 
                : 1L;
        
        // 计算分片边界
        Long shardIndex = commentShardUtils.calculateShardIndex(startSequenceNo);
        Long shardStartSeq = shardIndex * CommentConstants.SHARD_SIZE + 1;
        Long shardEndSeq = (shardIndex + 1) * CommentConstants.SHARD_SIZE;
        
        // 3. 查询该分片内的评论
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getContentId, contentId)
               .eq(Comment::getDeleted, 0)
               .ge(Comment::getSequenceNo, shardStartSeq)
               .le(Comment::getSequenceNo, shardEndSeq)
               .orderByAsc(Comment::getSequenceNo)
               .last("LIMIT " + pageSize);
        
        comments = commentMapper.selectList(wrapper);
        
        log.info("获取评论列表 contentId={}, startSeq={}, size={}", contentId, startSequenceNo, comments.size());
        return Result.success(comments);
    }

    @Override
    @Cacheable(value = "comment", key = "#commentId")
    public Result<Comment> getComment(Long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null || comment.getDeleted() == 1) {
            return Result.error("评论不存在");
        }
        return Result.success(comment);
    }

    @Override
    @Transactional
    public Result<String> delete(Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null || comment.getDeleted() == 1) {
            return Result.error("评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            return Result.error("无权删除此评论");
        }
        
        LambdaUpdateWrapper<Comment> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Comment::getId, commentId)
               .set(Comment::getDeleted, 1)
               .set(Comment::getUpdateTime, LocalDateTime.now());
        commentMapper.update(null, wrapper);
        
        log.info("删除评论 commentId={}, userId={}", commentId, userId);
        return Result.success("删除成功");
    }
}
