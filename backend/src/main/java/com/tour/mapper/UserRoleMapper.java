package com.tour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tour.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关联数据访问层
 *
 * <p>继承 MyBatis-Plus BaseMapper，提供对 user_roles 表的 CRUD 能力。</p>
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
}
