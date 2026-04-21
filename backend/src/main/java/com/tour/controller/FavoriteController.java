package com.tour.controller;

import com.tour.common.R;
import com.tour.entity.User;
import com.tour.mapper.UserMapper;
import com.tour.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Favorite", description = "Favorite endpoints")
@RestController
@RequestMapping("/posts")
@CrossOrigin
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserMapper userMapper;

    @Operation(summary = "Favorite a post")
    @PostMapping("/{id}/favorite")
    public R<Void> favorite(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        favoriteService.favorite(userId, id);
        return R.success();
    }

    @Operation(summary = "Unfavorite a post")
    @DeleteMapping("/{id}/favorite")
    public R<Void> unfavorite(@PathVariable Long id,
                               @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        favoriteService.unfavorite(userId, id);
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
