package com.tour.controller;

import com.tour.common.R;
import com.tour.entity.User;
import com.tour.mapper.UserMapper;
import com.tour.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 点赞控制器 —— 对帖子进行点赞/取消点赞
 */
@Tag(name = "点赞", description = "帖子点赞与取消点赞接口")
@RestController
@RequestMapping("/posts")
@CrossOrigin
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserMapper userMapper;

    /** 点赞帖子（需要登录） */
    @Operation(summary = "点赞", description = "对指定帖子点赞，每个用户只能点一次")
    @PostMapping("/{id}/like")
    public R<Void> like(
            @Parameter(description = "帖子 ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        likeService.like(userId, id);
        return R.success();
    }

    /** 取消点赞（需要登录） */
    @Operation(summary = "取消点赞", description = "取消对指定帖子的点赞")
    @DeleteMapping("/{id}/like")
    public R<Void> unlike(
            @Parameter(description = "帖子 ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        likeService.unlike(userId, id);
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
