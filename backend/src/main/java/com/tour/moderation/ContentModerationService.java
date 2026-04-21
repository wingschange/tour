package com.tour.moderation;

/**
 * 内容审核服务接口
 *
 * <p>在帖子发布前调用，对文本和图片进行合规检查。
 * 当前为桩实现（{@code DummyContentModerationServiceImpl}），始终返回允许。
 * 未来可接入阿里云内容安全或其他 AI 审核服务。</p>
 */
public interface ContentModerationService {

    /**
     * 检查文本内容是否合规
     *
     * @param content 待审核的文本内容
     * @return true=允许发布，false=违规拒绝
     */
    boolean isContentAllowed(String content);

    /**
     * 检查图片是否合规
     *
     * @param imageUrl 待审核的图片 URL
     * @return true=允许，false=违规拒绝
     */
    boolean isImageAllowed(String imageUrl);
}
