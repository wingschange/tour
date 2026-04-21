package com.tour.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tour.common.R;
import com.tour.entity.User;
import com.tour.mapper.UserMapper;
import com.tour.service.FavoriteService;
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
 * 用户控制器 —— 个人资料、关注、收藏
 */
@Tag(name = "用户", description = "用户资料查询、修改、关注及收藏接口")
@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserMapper userMapper;

    /** 查询用户资料（公开接口） */
    @Operation(summary = "获取用户资料", description = "根据用户 ID 获取公开资料，无需登录")
    @GetMapping("/{id}")
    public R<UserVo> getProfile(@Parameter(description = "用户 ID") @PathVariable Long id) {
        return R.success(userService.getProfile(id));
    }

    /** 修改用户资料（需要登录） */
    @Operation(summary = "修改用户资料", description = "修改当前用户的昵称、头像、简介等信息")
    @PutMapping("/{id}")
    public R<Void> updateProfile(
            @Parameter(description = "用户 ID") @PathVariable Long id,
            @RequestBody User user,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.updateProfile(id, user);
        return R.success();
    }

    /** 关注用户（需要登录） */
    @Operation(summary = "关注用户", description = "关注指定用户")
    @PostMapping("/{id}/follow")
    public R<Void> follow(
            @Parameter(description = "被关注用户 ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long followerId = getUserIdFromDetails(userDetails);
        userService.follow(followerId, id);
        return R.success();
    }

    /** 取消关注（需要登录） */
    @Operation(summary = "取消关注", description = "取消对指定用户的关注")
    @DeleteMapping("/{id}/follow")
    public R<Void> unfollow(
            @Parameter(description = "被取消关注的用户 ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long followerId = getUserIdFromDetails(userDetails);
        userService.unfollow(followerId, id);
        return R.success();
    }

    /** 获取粉丝列表 */
    @Operation(summary = "粉丝列表", description = "获取指定用户的所有粉丝")
    @GetMapping("/{id}/followers")
    public R<List<UserVo>> getFollowers(@Parameter(description = "用户 ID") @PathVariable Long id) {
        return R.success(userService.getFollowers(id));
    }

    /** 获取关注列表 */
    @Operation(summary = "关注列表", description = "获取指定用户关注的所有人")
    @GetMapping("/{id}/following")
    public R<List<UserVo>> getFollowing(@Parameter(description = "用户 ID") @PathVariable Long id) {
        return R.success(userService.getFollowing(id));
    }

    /** 获取用户收藏的帖子列表 */
    @Operation(summary = "收藏列表", description = "获取指定用户的收藏帖子，分页返回")
    @GetMapping("/{id}/favorites")
    public R<?> getFavorites(
            @Parameter(description = "用户 ID") @PathVariable Long id,
            @Parameter(description = "页码，默认 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页条数，默认 10") @RequestParam(defaultValue = "10") int size) {
        return R.success(favoriteService.listFavorites(id, page, size));
    }

    /** 根据 UserDetails 查询用户 ID */
    private Long getUserIdFromDetails(UserDetails userDetails) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, userDetails.getUsername()));
        if (user == null) throw new IllegalArgumentException("用户不存在");
        return user.getId();
    }
}
