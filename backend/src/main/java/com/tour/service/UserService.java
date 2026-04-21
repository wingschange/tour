package com.tour.service;

import com.tour.dto.LoginDto;
import com.tour.dto.RegisterDto;
import com.tour.entity.User;
import com.tour.vo.UserVo;

import java.util.List;

/**
 * 用户服务接口 —— 注册、登录、资料管理、关注关系
 */
public interface UserService {

    /**
     * 注册新用户，默认分配 ROLE_USER 角色
     *
     * @param registerDto 注册信息（用户名、密码、邮箱）
     */
    void register(RegisterDto registerDto);

    /**
     * 用户登录，验证通过后返回 JWT Token
     *
     * @param loginDto 登录凭证
     * @return JWT Token 字符串
     */
    String login(LoginDto loginDto);

    /**
     * 根据用户 ID 查询公开资料
     *
     * @param userId 用户 ID
     * @return 用户视图对象（不含密码）
     */
    UserVo getProfile(Long userId);

    /**
     * 更新用户资料（头像、昵称、简介等）
     *
     * @param userId 用户 ID
     * @param user   需要更新的字段
     */
    void updateProfile(Long userId, User user);

    /**
     * 关注用户
     *
     * @param followerId 关注者（粉丝）ID
     * @param followeeId 被关注者 ID
     */
    void follow(Long followerId, Long followeeId);

    /**
     * 取消关注
     *
     * @param followerId 关注者 ID
     * @param followeeId 被关注者 ID
     */
    void unfollow(Long followerId, Long followeeId);

    /**
     * 获取指定用户的粉丝列表
     *
     * @param userId 用户 ID
     * @return 粉丝用户视图列表
     */
    List<UserVo> getFollowers(Long userId);

    /**
     * 获取指定用户的关注列表
     *
     * @param userId 用户 ID
     * @return 关注用户视图列表
     */
    List<UserVo> getFollowing(Long userId);
}
