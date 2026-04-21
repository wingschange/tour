package com.tour.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tour.common.R;
import com.tour.entity.User;
import com.tour.mapper.UserMapper;
import com.tour.service.FavoriteService;
import com.tour.service.UserService;
import com.tour.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "User management endpoints")
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

    @Operation(summary = "Get user profile by ID")
    @GetMapping("/{id}")
    public R<UserVo> getProfile(@PathVariable Long id) {
        return R.success(userService.getProfile(id));
    }

    @Operation(summary = "Update user profile")
    @PutMapping("/{id}")
    public R<Void> updateProfile(@PathVariable Long id,
                                  @RequestBody User user,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        userService.updateProfile(id, user);
        return R.success();
    }

    @Operation(summary = "Follow a user")
    @PostMapping("/{id}/follow")
    public R<Void> follow(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails) {
        Long followerId = getUserIdFromDetails(userDetails);
        userService.follow(followerId, id);
        return R.success();
    }

    @Operation(summary = "Unfollow a user")
    @DeleteMapping("/{id}/follow")
    public R<Void> unfollow(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails) {
        Long followerId = getUserIdFromDetails(userDetails);
        userService.unfollow(followerId, id);
        return R.success();
    }

    @Operation(summary = "Get followers of a user")
    @GetMapping("/{id}/followers")
    public R<List<UserVo>> getFollowers(@PathVariable Long id) {
        return R.success(userService.getFollowers(id));
    }

    @Operation(summary = "Get users that a user is following")
    @GetMapping("/{id}/following")
    public R<List<UserVo>> getFollowing(@PathVariable Long id) {
        return R.success(userService.getFollowing(id));
    }

    @Operation(summary = "Get user favorites")
    @GetMapping("/{id}/favorites")
    public R<?> getFavorites(@PathVariable Long id,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size) {
        return R.success(favoriteService.listFavorites(id, page, size));
    }

    private Long getUserIdFromDetails(UserDetails userDetails) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, userDetails.getUsername()));
        if (user == null) throw new IllegalArgumentException("User not found");
        return user.getId();
    }
}
