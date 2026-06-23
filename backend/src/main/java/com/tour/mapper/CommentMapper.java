package com.tour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tour.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论数据访问层
 *
 * <p>继承 MyBatis-Plus BaseMapper，提供对 comments 表的 CRUD 能力。</p>
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
