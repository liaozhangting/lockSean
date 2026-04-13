package com.lssm.controller;

import com.lssm.dto.CommentDTO;
import com.lssm.entity.Comment;
import com.lssm.service.CommentService;
import com.lssm.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comment")
@Tag(name = "评论接口", description = "评论发布、查询、删除")
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    @Operation(summary = "发布评论")
    @PostMapping("/publish")
    public Result<String> publish(@RequestBody @Valid CommentDTO commentDTO) {
        return commentService.publish(commentDTO);
    }

    @Operation(summary = "获取评论列表（分片查询）")
    @GetMapping("/list/{contentId}")
    public Result<List<Comment>> getComments(
            @Parameter(description = "内容ID") @PathVariable Long contentId,
            @Parameter(description = "最后一条评论序列号，用于分页") @RequestParam(required = false) Long lastSequenceNo,
            @Parameter(description = "每页数量，默认10") @RequestParam(defaultValue = "10") Integer pageSize) {
        return commentService.getComments(contentId, lastSequenceNo, pageSize);
    }

    @Operation(summary = "获取评论详情")
    @GetMapping("/get/{commentId}")
    public Result<Comment> getComment(
            @Parameter(description = "评论ID") @PathVariable Long commentId) {
        return commentService.getComment(commentId);
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/delete/{commentId}")
    public Result<String> delete(
            @Parameter(description = "评论ID") @PathVariable Long commentId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        return commentService.delete(commentId, userId);
    }
}
