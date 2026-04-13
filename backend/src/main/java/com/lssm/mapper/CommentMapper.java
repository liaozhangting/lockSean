package com.lssm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lssm.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    
    /**
     * 查询某个内容下的最大序列号
     */
    @Select("SELECT MAX(sequence_no) FROM comment WHERE content_id = #{contentId} AND deleted = 0")
    Long selectMaxSequenceNo(@Param("contentId") Long contentId);
}
