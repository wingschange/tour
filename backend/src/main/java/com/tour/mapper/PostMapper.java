package com.tour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tour.entity.Post;
import org.apache.ibatis.annotations.Mapper;

/**
 * 帖子数据访问层
 *
 * <p>继承 MyBatis-Plus BaseMapper，提供对 posts 表的 CRUD 能力。</p>
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
