package com.lssm.service;

import com.lssm.dto.ContentDTO;
import com.lssm.dto.PageCursorDTO;
import com.lssm.entity.Content;
import com.lssm.entity.vo.ContentVO;
import com.lssm.utils.Result;
import jakarta.validation.Valid;

import java.util.List;

public interface ContentService {
    void init();
    Result<String> publish(ContentDTO contentDTO);

    Result<ContentVO> get(long contentId);

    /**
     * 首页推荐内容（游标分页）
     * @param pageCursor 游标分页参数
     * @return 内容列表
     */
    Result<List<Content>> getRecommendContents(PageCursorDTO pageCursor);

    /**
     * 获取关注内容
     */
    Result<List<ContentVO>> getFollowingContent(Integer page, Integer pageSize);
    
    /**
     * 获取用户内容列表（游标分页）
     */
    Result<List<Content>> getUserContents(PageCursorDTO pageCursor);
}
