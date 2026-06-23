package com.tour.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tour.vo.PostVo;

/**
 * 收藏服务接口 —— 收藏/取消收藏帖子、查询收藏状态与列表
 */
public interface FavoriteService {

    /**
     * 收藏指定帖子（幂等：已收藏则直接返回）
     *
     * @param userId 用户 ID
     * @param postId 帖子 ID
     */
    void favorite(Long userId, Long postId);

    /**
     * 取消收藏指定帖子
     *
     * @param userId 用户 ID
     * @param postId 帖子 ID
     */
    void unfavorite(Long userId, Long postId);

    /**
     * 查询用户是否已收藏指定帖子
     *
     * @param userId 用户 ID
     * @param postId 帖子 ID
     * @return true=已收藏，false=未收藏
     */
    boolean isFavorited(Long userId, Long postId);

    /**
     * 分页查询用户的收藏帖子列表
     *
     * @param userId 用户 ID
     * @param page   页码
     * @param size   每页条数
     * @return 帖子视图分页结果
     */
    IPage<PostVo> listFavorites(Long userId, int page, int size);
}
