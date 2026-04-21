package com.tour.controller;

import com.tour.common.R;
import com.tour.entity.User;
import com.tour.mapper.UserMapper;
import com.tour.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "Like endpoints")
@RestController
@RequestMapping("/posts")
@CrossOrigin
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserMapper userMapper;

    @Operation(summary = "Like a post")
    @PostMapping("/{id}/like")
    public R<Void> like(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        likeService.like(userId, id);
        return R.success();
    }

    @Operation(summary = "Unlike a post")
    @DeleteMapping("/{id}/like")
    public R<Void> unlike(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        likeService.unlike(userId, id);
        return R.success();
    }

    private Long getUserId(UserDetails userDetails) {
        User user = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                        .eq(User::getUsername, userDetails.getUsername()));
        if (user == null) throw new IllegalArgumentException("User not found");
        return user.getId();
    }
}
