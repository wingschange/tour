package com.tour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tour.entity.Follow;
import org.apache.ibatis.annotations.Mapper;

/**
 * 关注关系数据访问层
 *
 * <p>继承 MyBatis-Plus BaseMapper，提供对 follows 表的 CRUD 能力。</p>
 */
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {
}
