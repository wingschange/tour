package com.tour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tour.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层
 *
 * <p>继承 MyBatis-Plus BaseMapper，提供对 users 表的 CRUD 能力。</p>
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
