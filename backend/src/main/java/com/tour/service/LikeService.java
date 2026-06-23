package com.tour.service;

/**
 * 点赞服务接口 —— 对帖子进行点赞/取消点赞
 */
public interface LikeService {

    /**
     * 点赞指定帖子（幂等：已点赞则直接返回）
     *
     * @param userId 用户 ID
     * @param postId 帖子 ID
     */
    void like(Long userId, Long postId);

    /**
     * 取消点赞指定帖子
     *
     * @param userId 用户 ID
     * @param postId 帖子 ID
     */
    void unlike(Long userId, Long postId);

    /**
     * 查询用户是否已点赞指定帖子
     *
     * @param userId 用户 ID
     * @param postId 帖子 ID
     * @return true=已点赞，false=未点赞
     */
    boolean isLiked(Long userId, Long postId);
}
