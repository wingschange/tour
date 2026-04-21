package com.tour.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tour.common.R;
import com.tour.dto.PostCreateDto;
import com.tour.entity.User;
import com.tour.mapper.UserMapper;
import com.tour.service.PostService;
import com.tour.vo.PostVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Post", description = "Post management endpoints")
@RestController
@RequestMapping("/posts")
@CrossOrigin
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserMapper userMapper;

    @Operation(summary = "Create a new post")
    @PostMapping
    public R<PostVo> createPost(@Valid @RequestBody PostCreateDto dto,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return R.success(postService.createPost(userId, dto));
    }

    @Operation(summary = "List posts (public, paginated)")
    @GetMapping
    public R<IPage<PostVo>> listPosts(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(required = false) Long sectionId,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = userDetails != null ? getUserId(userDetails) : null;
        return R.success(postService.listPosts(page, size, sectionId, currentUserId));
    }

    @Operation(summary = "Get post by ID")
    @GetMapping("/{id}")
    public R<PostVo> getPost(@PathVariable Long id,
                              @AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = userDetails != null ? getUserId(userDetails) : null;
        return R.success(postService.getPost(id, currentUserId));
    }

    @Operation(summary = "Delete a post")
    @DeleteMapping("/{id}")
    public R<Void> deletePost(@PathVariable Long id,
                               @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        postService.deletePost(id, userId);
        return R.success();
    }

    @Operation(summary = "Get feed posts")
    @GetMapping("/feed")
    public R<IPage<PostVo>> getFeed(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return R.success(postService.listFeedPosts(userId, page, size));
    }

    private Long getUserId(UserDetails userDetails) {
        User user = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                        .eq(User::getUsername, userDetails.getUsername()));
        if (user == null) throw new IllegalArgumentException("User not found");
        return user.getId();
    }
}
