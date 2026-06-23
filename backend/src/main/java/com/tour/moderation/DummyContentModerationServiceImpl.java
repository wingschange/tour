package com.tour.moderation;

import org.springframework.stereotype.Service;

/**
 * 内容审核服务桩实现
 *
 * <p>默认实现，不对内容进行任何拦截，始终返回通过。
 * 生产环境建议替换为真实的内容安全服务（如阿里云内容安全）。</p>
 */
@Service
public class DummyContentModerationServiceImpl implements ContentModerationService {

    /** 默认允许所有文本内容 */
    @Override
    public boolean isContentAllowed(String content) {
        return true;
    }

    /** 默认允许所有图片内容 */
    @Override
    public boolean isImageAllowed(String imageUrl) {
        return true;
    }
}
