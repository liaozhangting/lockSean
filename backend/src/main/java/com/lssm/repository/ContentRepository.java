package com.lssm.repository;

import com.lssm.entity.ContentDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 内容 ES 仓储层
 */
@Repository
public interface ContentRepository extends ElasticsearchRepository<ContentDocument, Long> {

    /**
     * 根据关键词搜索内容（标题+正文）
     */
    List<ContentDocument> findByTitleContainingOrContentContaining(String title, String content);

    /**
     * 根据作者ID搜索内容
     */
    List<ContentDocument> findByUserId(Long authorId);

    /**
     * 删除作者的所有内容索引
     */
    void deleteByUserId(Long authorId);
}
