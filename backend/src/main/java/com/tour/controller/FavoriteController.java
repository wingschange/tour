package com.tour.controller;

import com.tour.common.R;
import com.tour.entity.User;
import com.tour.mapper.UserMapper;
import com.tour.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏控制器 —— 收藏/取消收藏帖子
 */
@Tag(name = "收藏", description = "帖子收藏与取消收藏接口")
@RestController
@RequestMapping("/posts")
@CrossOrigin
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserMapper userMapper;

    /** 收藏帖子（需要登录） */
    @Operation(summary = "收藏帖子", description = "将指定帖子加入收藏，每个用户只能收藏一次")
    @PostMapping("/{id}/favorite")
    public R<Void> favorite(
            @Parameter(description = "帖子 ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        favoriteService.favorite(userId, id);
        return R.success();
    }

    /** 取消收藏（需要登录） */
    @Operation(summary = "取消收藏", description = "取消对指定帖子的收藏")
    @DeleteMapping("/{id}/favorite")
    public R<Void> unfavorite(
            @Parameter(description = "帖子 ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        favoriteService.unfavorite(userId, id);
        return R.success();
    }

    /** 根据 UserDetails 查询用户 ID */
    private Long getUserId(UserDetails userDetails) {
        User user = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                        .eq(User::getUsername, userDetails.getUsername()));
        if (user == null) throw new IllegalArgumentException("用户不存在");
        return user.getId();
    }
}
