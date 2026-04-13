package com.lssm.controller;

import com.lssm.dto.ContentDTO;
import com.lssm.dto.PageCursorDTO;
import com.lssm.entity.Content;
import com.lssm.entity.vo.ContentVO;
import com.lssm.service.ContentService;
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
@RequestMapping("/content")
@Tag(name = "内容接口", description = "内容发布、查询、搜索、互动")
public class ContentController {
    
    @Autowired
    private ContentService contentService;

    @Operation(summary = "发布内容")
    @PostMapping("/publish")
    public Result<String> publish(@RequestBody @Valid ContentDTO contentDTO) {
        return contentService.publish(contentDTO);
    }

    @Operation(summary = "获取内容详情")
    @GetMapping("/get/{contentId}")
    public Result<ContentVO> getContent(
            @Parameter(description = "内容ID") @PathVariable long contentId) {
        return contentService.get(contentId);
    }

    @Operation(summary = "获取首页推荐内容（游标分页）")
    @GetMapping("/home/recommend")
    public Result<List<Content>> getRecommendContents(
            @Parameter(description = "最后一条内容ID，用于游标分页") @RequestParam(required = false) Long lastId,
            @Parameter(description = "每页数量，默认10") @RequestParam(defaultValue = "10") Integer pageSize) {
        PageCursorDTO pageCursor = new PageCursorDTO();
        pageCursor.setLastId(lastId);
        pageCursor.setPageSize(pageSize);
        return contentService.getRecommendContents(pageCursor);
    }

    @Operation(summary = "获取关注内容")
    @GetMapping("/following")
    public Result<List<ContentVO>> getFollowingContent(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return contentService.getFollowingContent(page, pageSize);
    }

    @Operation(summary = "获取用户内容列表（游标分页）")
    @GetMapping("/user/{userId}")
    public Result<List<Content>> getUserContents(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "最后一条内容ID，用于游标分页") @RequestParam(required = false) Long lastId,
            @Parameter(description = "每页数量，默认10") @RequestParam(defaultValue = "10") Integer pageSize) {
        PageCursorDTO pageCursor = new PageCursorDTO();
        pageCursor.setUserId(userId);
        pageCursor.setLastId(lastId);
        pageCursor.setPageSize(pageSize);
        return contentService.getUserContents(pageCursor);
    }
}
