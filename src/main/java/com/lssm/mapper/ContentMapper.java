package com.lssm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lssm.entity.Content;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ContentMapper extends BaseMapper<Content> {
    
    /**
     * 游标分页查询（基于自增ID）
     * 避免深分页的全表扫描问题
     */
    @Select("SELECT * FROM tb_content " +
            "WHERE deleted = 0 " +
            "AND id < #{lastId} " +
            "ORDER BY id DESC " +
            "LIMIT #{pageSize}")
    Page<Content> selectCursorPage(@Param("lastId") Long lastId, @Param("pageSize") Integer pageSize);
    
    /**
     * 游标分页查询（带筛选条件）
     */
    @Select("SELECT * FROM tb_content " +
            "WHERE deleted = 0 " +
            "AND id < #{lastId} " +
            "AND (#{userId} IS NULL OR author_id = #{userId}) " +
            "AND (#{type} IS NULL OR type = #{type}) " +
            "ORDER BY id DESC " +
            "LIMIT #{pageSize}")
    Page<Content> selectCursorPageWithFilters(@Param("lastId") Long lastId, 
                                               @Param("userId") Long userId,
                                               @Param("type") String type,
                                               @Param("pageSize") Integer pageSize);
    
    /**
     * 首页推荐游标分页（按ID倒序）
     */
    @Select("SELECT * FROM tb_content " +
            "WHERE deleted = 0 " +
            "AND id < #{lastId} " +
            "ORDER BY id DESC " +
            "LIMIT #{pageSize}")
    Page<Content> selectHomeRecommendCursor(@Param("lastId") Long lastId, @Param("pageSize") Integer pageSize);
    
    /**
     * 统计总数（用于分页导航）
     */
    @Select("SELECT COUNT(*) FROM tb_content WHERE deleted = 0")
    Long selectTotalCount();
}
