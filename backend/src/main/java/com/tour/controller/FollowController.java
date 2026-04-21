package com.tour.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tour.common.R;
import com.tour.entity.User;
import com.tour.mapper.UserMapper;
import com.tour.service.UserService;
import com.tour.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 关注控制器 —— 关注/取消关注/粉丝/关注列表
 * <p>与 UserController 中关注相关接口功能相同，保留兼容路由</p>
 */
@Tag(name = "关注", description = "关注、取消关注、粉丝与关注列表接口")
@RestController
@CrossOrigin
public class FollowController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    /** 关注用户（需要登录） */
    @Operation(summary = "关注用户", description = "关注指定用户")
    @PostMapping("/users/{id}/follow")
    public R<Void> follow(
            @Parameter(description = "被关注用户 ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long followerId = getUserId(userDetails);
        userService.follow(followerId, id);
        return R.success();
    }

    /** 取消关注（需要登录） */
    @Operation(summary = "取消关注", description = "取消对指定用户的关注")
    @DeleteMapping("/users/{id}/follow")
    public R<Void> unfollow(
            @Parameter(description = "被取消关注的用户 ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long followerId = getUserId(userDetails);
        userService.unfollow(followerId, id);
        return R.success();
    }

    /** 获取粉丝列表 */
    @Operation(summary = "粉丝列表", description = "获取指定用户的所有粉丝")
    @GetMapping("/users/{id}/followers")
    public R<List<UserVo>> getFollowers(
            @Parameter(description = "用户 ID") @PathVariable Long id) {
        return R.success(userService.getFollowers(id));
    }

    /** 获取关注列表 */
    @Operation(summary = "关注列表", description = "获取指定用户关注的所有人")
    @GetMapping("/users/{id}/following")
    public R<List<UserVo>> getFollowing(
            @Parameter(description = "用户 ID") @PathVariable Long id) {
        return R.success(userService.getFollowing(id));
    }

    /** 根据 UserDetails 查询用户 ID */
    private Long getUserId(UserDetails userDetails) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, userDetails.getUsername()));
        if (user == null) throw new IllegalArgumentException("用户不存在");
        return user.getId();
    }
}
