package com.lssm.service;

import com.lssm.dto.SearchDTO;
import com.lssm.entity.ContentDocument;
import com.lssm.utils.Result;
import java.util.List;

public interface ContentSearchService {
    
    /**
     * 全文检索
     */
    Result<List<ContentDocument>> search(SearchDTO searchDTO);
    
    /**
     * 同步内容到 ES（MQ 调用）
     */
    void syncToEs(Long contentId);
    
    /**
     * 从 ES 删除内容
     */
    void deleteFromEs(Long contentId);
    
    /**
     * 重建全部索引
     */
    Result<String> rebuildIndex();
}
