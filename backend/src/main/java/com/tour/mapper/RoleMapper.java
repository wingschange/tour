package com.tour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tour.entity.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色数据访问层
 *
 * <p>继承 MyBatis-Plus BaseMapper，提供对 roles 表的 CRUD 能力。</p>
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
