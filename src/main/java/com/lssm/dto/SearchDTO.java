package com.lssm.dto;

import lombok.Data;

@Data
public class SearchDTO {
    
    /** 搜索关键词 */
    private String keyword;
    
    /** 搜索类型：title-标题，content-内容，all-全部 */
    private String searchType = "all";
    
    /** 用户ID筛选 */
    private Long userId;
    
    /** 内容类型 */
    private String type;
    
    /** 页码，从1开始 */
    private Integer page = 1;
    
    /** 每页数量 */
    private Integer pageSize = 20;
}
