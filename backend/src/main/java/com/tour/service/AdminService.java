package com.tour.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tour.entity.Post;
import com.tour.entity.Section;
import com.tour.vo.UserVo;

/**
 * 管理员服务接口 —— 用户、帖子、板块的后台管理
 *
 * <p>所有方法均需要调用方具备 ROLE_ADMIN 权限。</p>
 */
public interface AdminService {

    /**
     * 分页查询所有注册用户
     *
     * @param page 页码（从 1 开始）
     * @param size 每页条数
     * @return 用户视图分页结果
     */
    IPage<UserVo> listUsers(int page, int size);

    /**
     * 删除指定用户（逻辑删除）
     *
     * @param userId 用户 ID
     */
    void deleteUser(Long userId);

    /**
     * 修改指定用户的角色
     *
     * @param userId   用户 ID
     * @param roleName 角色名称，如 ROLE_USER / ROLE_MODERATOR / ROLE_ADMIN
     */
    void updateUserRole(Long userId, String roleName);

    /**
     * 分页查询所有帖子（管理后台，包含已删除）
     *
     * @param page 页码
     * @param size 每页条数
     * @return 帖子分页结果
     */
    IPage<Post> listAllPosts(int page, int size);

    /**
     * 管理员强制删除指定帖子
     *
     * @param postId 帖子 ID
     */
    void deletePost(Long postId);

    /**
     * 分页查询所有板块
     *
     * @param page 页码
     * @param size 每页条数
     * @return 板块分页结果
     */
    IPage<Section> listSections(int page, int size);
}
