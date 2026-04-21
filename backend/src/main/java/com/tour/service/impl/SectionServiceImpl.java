package com.tour.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tour.entity.Section;
import com.tour.mapper.SectionMapper;
import com.tour.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionMapper sectionMapper;

    @Override
    public IPage<Section> listSections(int page, int size) {
        return sectionMapper.selectPage(new Page<>(page, size), null);
    }

    @Override
    public Section getSection(Long sectionId) {
        Section section = sectionMapper.selectById(sectionId);
        if (section == null) {
            throw new IllegalArgumentException("Section not found");
        }
        return section;
    }

    @Override
    @Transactional
    public Section createSection(Section section) {
        section.setPostCount(0);
        sectionMapper.insert(section);
        return section;
    }

    @Override
    @Transactional
    public Section updateSection(Long sectionId, Section updated) {
        Section existing = sectionMapper.selectById(sectionId);
        if (existing == null) {
            throw new IllegalArgumentException("Section not found");
        }
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getProvince() != null) existing.setProvince(updated.getProvince());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        if (updated.getCoverImage() != null) existing.setCoverImage(updated.getCoverImage());
        sectionMapper.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void deleteSection(Long sectionId) {
        Section section = sectionMapper.selectById(sectionId);
        if (section == null) {
            throw new IllegalArgumentException("Section not found");
        }
        sectionMapper.deleteById(sectionId);
    }
}
