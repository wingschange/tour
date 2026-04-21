package com.tour.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tour.common.R;
import com.tour.dto.PostCreateDto;
import com.tour.entity.User;
import com.tour.mapper.UserMapper;
import com.tour.service.PostService;
import com.tour.vo.PostVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 帖子控制器 —— 发帖、查帖、删帖、信息流
 */
@Tag(name = "帖子", description = "帖子发布、查询、删除及信息流接口")
@RestController
@RequestMapping("/posts")
@CrossOrigin
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserMapper userMapper;

    /** 发布新帖子（需要登录） */
    @Operation(summary = "发布帖子", description = "创建一篇新的旅游帖子，需要登录")
    @PostMapping
    public R<PostVo> createPost(@Valid @RequestBody PostCreateDto dto,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return R.success(postService.createPost(userId, dto));
    }

    /** 分页获取帖子列表（公开接口） */
    @Operation(summary = "帖子列表", description = "分页查询帖子，支持按板块筛选，无需登录")
    @GetMapping
    public R<IPage<PostVo>> listPosts(
            @Parameter(description = "页码，默认 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页条数，默认 10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "板块 ID，不传则查询全部") @RequestParam(required = false) Long sectionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = userDetails != null ? getUserId(userDetails) : null;
        return R.success(postService.listPosts(page, size, sectionId, currentUserId));
    }

    /** 根据 ID 获取帖子详情（公开接口） */
    @Operation(summary = "帖子详情", description = "根据帖子 ID 获取详情，无需登录")
    @GetMapping("/{id}")
    public R<PostVo> getPost(
            @Parameter(description = "帖子 ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = userDetails != null ? getUserId(userDetails) : null;
        return R.success(postService.getPost(id, currentUserId));
    }

    /** 删除帖子（本人或管理员） */
    @Operation(summary = "删除帖子", description = "删除指定帖子，仅本人或管理员可操作")
    @DeleteMapping("/{id}")
    public R<Void> deletePost(
            @Parameter(description = "帖子 ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        postService.deletePost(id, userId);
        return R.success();
    }

    /** 获取关注用户的信息流（需要登录） */
    @Operation(summary = "我的信息流", description = "获取当前用户所关注的人发布的帖子，按时间倒序")
    @GetMapping("/feed")
    public R<IPage<PostVo>> getFeed(
            @Parameter(description = "页码，默认 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页条数，默认 10") @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return R.success(postService.listFeedPosts(userId, page, size));
    }

    /** 根据用户名查询用户 ID */
    private Long getUserId(UserDetails userDetails) {
        User user = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                        .eq(User::getUsername, userDetails.getUsername()));
        if (user == null) throw new IllegalArgumentException("用户不存在");
        return user.getId();
    }
}
