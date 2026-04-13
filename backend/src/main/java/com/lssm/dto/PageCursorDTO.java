package com.lssm.dto;

import lombok.Data;

@Data
public class PageCursorDTO {
    
    /** 最后一条内容的ID（游标） */
    private Long lastId;
    
    /** 最后一条内容的创建时间（辅助游标） */
    private Long lastCreateTime;
    
    /** 每页数量 */
    private Integer pageSize = 10;
    
    /** 筛选条件：用户ID */
    private Long userId;
    
    /** 筛选条件：内容类型 */
    private String type;
}
