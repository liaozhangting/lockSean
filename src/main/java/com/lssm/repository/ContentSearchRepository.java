package com.lssm.repository;

import com.lssm.entity.ContentDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentSearchRepository extends ElasticsearchRepository<ContentDocument, Long> {
    
    /**
     * 根据关键词搜索内容
     */
    List<ContentDocument> findByTitleContainingOrContentContaining(String title, String content);
    
    /**
     * 根据用户ID搜索
     */
    List<ContentDocument> findByUserId(Long userId);
    
    /**
     * 根据类型搜索
     */
    List<ContentDocument> findByType(String type);
}
