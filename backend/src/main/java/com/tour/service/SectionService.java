package com.tour.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tour.entity.Section;

/**
 * 板块服务接口 —— 旅游板块的查询、创建、修改、删除
 */
public interface SectionService {

    /**
     * 分页查询所有板块
     *
     * @param page 页码
     * @param size 每页条数
     * @return 板块分页结果
     */
    IPage<Section> listSections(int page, int size);

    /**
     * 根据 ID 查询板块详情
     *
     * @param sectionId 板块 ID
     * @return 板块实体
     */
    Section getSection(Long sectionId);

    /**
     * 创建新板块
     *
     * @param section 板块信息
     * @return 创建后的板块（含生成 ID）
     */
    Section createSection(Section section);

    /**
     * 修改板块信息
     *
     * @param sectionId 板块 ID
     * @param section   需要更新的字段
     * @return 更新后的板块
     */
    Section updateSection(Long sectionId, Section section);

    /**
     * 删除指定板块（逻辑删除）
     *
     * @param sectionId 板块 ID
     */
    void deleteSection(Long sectionId);
}
