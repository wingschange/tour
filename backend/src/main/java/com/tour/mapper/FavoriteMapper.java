package com.tour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tour.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏记录数据访问层
 *
 * <p>继承 MyBatis-Plus BaseMapper，提供对 favorites 表的 CRUD 能力。</p>
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}
