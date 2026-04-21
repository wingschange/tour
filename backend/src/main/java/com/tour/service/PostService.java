package com.tour.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tour.dto.PostCreateDto;
import com.tour.entity.Post;
import com.tour.vo.PostVo;

/**
 * 帖子服务接口 —— 发帖、查帖、删帖、信息流
 */
public interface PostService {

    /**
     * 发布新帖子
     *
     * @param userId 发布者用户 ID
     * @param dto    帖子内容
     * @return 帖子视图对象
     */
    PostVo createPost(Long userId, PostCreateDto dto);

    /**
     * 根据 ID 查询帖子详情，同时增加浏览次数
     *
     * @param postId        帖子 ID
     * @param currentUserId 当前登录用户 ID（未登录传 null）
     * @return 帖子视图对象
     */
    PostVo getPost(Long postId, Long currentUserId);

    /**
     * 分页查询帖子列表（支持按板块过滤）
     *
     * @param page          页码（从 1 开始）
     * @param size          每页条数
     * @param sectionId     板块 ID（为 null 时查全部）
     * @param currentUserId 当前登录用户 ID（未登录传 null）
     * @return 分页帖子列表
     */
    IPage<PostVo> listPosts(int page, int size, Long sectionId, Long currentUserId);

    /**
     * 获取关注用户的信息流帖子（按时间倒序）
     *
     * @param userId 当前用户 ID
     * @param page   页码
     * @param size   每页条数
     * @return 分页帖子列表
     */
    IPage<PostVo> listFeedPosts(Long userId, int page, int size);

    /**
     * 删除帖子（仅本人或管理员）
     *
     * @param postId        帖子 ID
     * @param currentUserId 当前用户 ID（用于权限校验）
     */
    void deletePost(Long postId, Long currentUserId);

    /**
     * 按板块分页查询帖子
     *
     * @param sectionId     板块 ID
     * @param page          页码
     * @param size          每页条数
     * @param currentUserId 当前登录用户 ID（未登录传 null）
     * @return 分页帖子列表
     */
    IPage<PostVo> listPostsBySection(Long sectionId, int page, int size, Long currentUserId);
}
