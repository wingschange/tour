package com.tour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tour.entity.Section;
import org.apache.ibatis.annotations.Mapper;

/**
 * 板块数据访问层
 *
 * <p>继承 MyBatis-Plus BaseMapper，提供对 sections 表的 CRUD 能力。</p>
 */
@Mapper
public interface SectionMapper extends BaseMapper<Section> {
}
