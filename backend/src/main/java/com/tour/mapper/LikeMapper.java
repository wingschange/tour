package com.tour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tour.entity.Like;
import org.apache.ibatis.annotations.Mapper;

/**
 * 点赞记录数据访问层
 *
 * <p>继承 MyBatis-Plus BaseMapper，提供对 likes 表的 CRUD 能力。</p>
 */
@Mapper
public interface LikeMapper extends BaseMapper<Like> {
}
