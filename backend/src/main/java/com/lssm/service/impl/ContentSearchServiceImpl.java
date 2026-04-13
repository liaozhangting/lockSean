package com.lssm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lssm.dto.SearchDTO;
import com.lssm.entity.Content;
import com.lssm.entity.ContentDocument;
import com.lssm.mapper.ContentMapper;
import com.lssm.repository.ContentSearchRepository;
import com.lssm.service.ContentSearchService;
import com.lssm.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContentSearchServiceImpl implements ContentSearchService {

    @Autowired
    private ContentSearchRepository contentSearchRepository;
    
    @Autowired
    private ContentMapper contentMapper;
    
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public Result<List<ContentDocument>> search(SearchDTO searchDTO) {
        if (searchDTO.getPage() == null || searchDTO.getPage() < 1) {
            searchDTO.setPage(1);
        }
        if (searchDTO.getPageSize() == null || searchDTO.getPageSize() <= 0) {
            searchDTO.setPageSize(20);
        }
        if (searchDTO.getPageSize() > 100) {
            searchDTO.setPageSize(100);
        }
        
        int from = (searchDTO.getPage() - 1) * searchDTO.getPageSize();
        
        try {
            Criteria criteria = new Criteria();
            boolean hasCondition = false;
            
            // 关键词搜索
            if (searchDTO.getKeyword() != null && !searchDTO.getKeyword().trim().isEmpty()) {
                String keyword = searchDTO.getKeyword().trim();
                if ("title".equals(searchDTO.getSearchType())) {
                    criteria = criteria.and("title").matches(keyword);
                } else if ("content".equals(searchDTO.getSearchType())) {
                    criteria = criteria.and("content").matches(keyword);
                } else {
                    criteria = criteria.and(new Criteria().or("title").matches(keyword).or("content").matches(keyword));
                }
                hasCondition = true;
            }
            
            // 用户ID筛选
            if (searchDTO.getUserId() != null) {
                criteria = criteria.and("userId").is(searchDTO.getUserId());
                hasCondition = true;
            }
            
            // 类型筛选
            if (searchDTO.getType() != null && !searchDTO.getType().trim().isEmpty()) {
                criteria = criteria.and("type").is(searchDTO.getType());
                hasCondition = true;
            }
            
            CriteriaQuery query;
            if (hasCondition) {
                query = new CriteriaQuery(criteria);
            } else {
                query = new CriteriaQuery(new Criteria());
            }
            
            query.setPageable(PageRequest.of(from / searchDTO.getPageSize(), searchDTO.getPageSize()));
            
            long startTime = System.currentTimeMillis();
            SearchHits<ContentDocument> searchHits = elasticsearchOperations.search(query, ContentDocument.class);
            long costTime = System.currentTimeMillis() - startTime;
            
            List<ContentDocument> results = searchHits.getSearchHits().stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList());
            
            log.info("ES搜索完成 keyword={}, total={}, cost={}ms", 
                    searchDTO.getKeyword(), searchHits.getTotalHits(), costTime);
            
            return Result.success(results);
        } catch (Exception e) {
            log.error("ES搜索失败 keyword={}", searchDTO.getKeyword(), e);
            return Result.error("搜索服务异常");
        }
    }

    @Override
    public void syncToEs(Long contentId) {
        try {
            Content content = contentMapper.selectById(contentId);
            if (content == null) {
                log.warn("同步内容到ES失败，内容不存在 contentId={}", contentId);
                return;
            }
            
            ContentDocument document = convertToDocument(content);
            contentSearchRepository.save(document);
            
            log.info("同步内容到ES成功 contentId={}", contentId);
        } catch (Exception e) {
            log.error("同步内容到ES失败 contentId={}", contentId, e);
            throw e;
        }
    }

    @Override
    public void deleteFromEs(Long contentId) {
        try {
            contentSearchRepository.deleteById(contentId);
            log.info("从ES删除内容成功 contentId={}", contentId);
        } catch (Exception e) {
            log.error("从ES删除内容失败 contentId={}", contentId, e);
            throw e;
        }
    }

    @Override
    public Result<String> rebuildIndex() {
        try {
            // 获取所有内容
            LambdaQueryWrapper<Content> wrapper = new LambdaQueryWrapper<>();
            List<Content> contents = contentMapper.selectList(wrapper);
            
            // 转换并批量保存
            List<ContentDocument> documents = contents.stream()
                    .map(this::convertToDocument)
                    .collect(Collectors.toList());
            
            contentSearchRepository.saveAll(documents);
            
            log.info("重建ES索引完成 total={}", documents.size());
            return Result.success("重建索引成功，共 " + documents.size() + " 条数据");
        } catch (Exception e) {
            log.error("重建ES索引失败", e);
            return Result.error("重建索引失败：" + e.getMessage());
        }
    }
    
    private ContentDocument convertToDocument(Content content) {
        ContentDocument doc = new ContentDocument();
        doc.setId(content.getId());
        doc.setUserId(content.getAuthorId());
        doc.setTitle(content.getTitle());
        doc.setContent(content.getContent());
        doc.setType(content.getType());
        doc.setMediaUrl(content.getVideoUrl());
        doc.setLikeCount(content.getLikeCount());
        doc.setCommentCount(content.getCommentCount());
        doc.setCollectCount(content.getCollectCount());
        doc.setLatitude(content.getLatitude());
        doc.setLongitude(content.getLongitude());
        doc.setLocation(content.getLocation());
        doc.setCreateTime(content.getCreateTime());
        doc.setUpdateTime(content.getUpdateTime());
        return doc;
    }
}
