package com.lssm.controller;

import com.lssm.dto.SearchDTO;
import com.lssm.entity.ContentDocument;
import com.lssm.service.ContentSearchService;
import com.lssm.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/search")
@Tag(name = "搜索接口", description = "全文检索")
public class SearchController {

    @Autowired
    private ContentSearchService contentSearchService;

    @Operation(summary = "全文搜索")
    @GetMapping
    public Result<List<ContentDocument>> search(SearchDTO searchDTO) {
        return contentSearchService.search(searchDTO);
    }

    @Operation(summary = "重建搜索索引")
    @PostMapping("/rebuild")
    public Result<String> rebuildIndex() {
        return contentSearchService.rebuildIndex();
    }
}
