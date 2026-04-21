package com.tour.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tour.common.R;
import com.tour.entity.User;
import com.tour.mapper.UserMapper;
import com.tour.service.UserService;
import com.tour.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Follow", description = "Follow/Unfollow endpoints")
@RestController
@CrossOrigin
public class FollowController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Operation(summary = "Follow a user")
    @PostMapping("/users/{id}/follow")
    public R<Void> follow(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails) {
        Long followerId = getUserId(userDetails);
        userService.follow(followerId, id);
        return R.success();
    }

    @Operation(summary = "Unfollow a user")
    @DeleteMapping("/users/{id}/follow")
    public R<Void> unfollow(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails) {
        Long followerId = getUserId(userDetails);
        userService.unfollow(followerId, id);
        return R.success();
    }

    @Operation(summary = "Get followers of a user")
    @GetMapping("/users/{id}/followers")
    public R<List<UserVo>> getFollowers(@PathVariable Long id) {
        return R.success(userService.getFollowers(id));
    }

    @Operation(summary = "Get users that a user is following")
    @GetMapping("/users/{id}/following")
    public R<List<UserVo>> getFollowing(@PathVariable Long id) {
        return R.success(userService.getFollowing(id));
    }

    private Long getUserId(UserDetails userDetails) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, userDetails.getUsername()));
        if (user == null) throw new IllegalArgumentException("User not found");
        return user.getId();
    }
}
