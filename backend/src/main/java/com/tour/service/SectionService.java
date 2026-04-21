package com.tour.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tour.entity.Section;

public interface SectionService {

    IPage<Section> listSections(int page, int size);

    Section getSection(Long sectionId);

    Section createSection(Section section);

    Section updateSection(Long sectionId, Section section);

    void deleteSection(Long sectionId);
}
