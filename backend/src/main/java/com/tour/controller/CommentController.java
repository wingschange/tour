package com.tour.controller;

import com.tour.common.R;
import com.tour.dto.CommentCreateDto;
import com.tour.entity.User;
import com.tour.mapper.UserMapper;
import com.tour.service.CommentService;
import com.tour.vo.CommentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 评论控制器 —— 发表、查询、删除评论
 */
@Tag(name = "评论", description = "评论的发表、查询与删除接口")
@RestController
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserMapper userMapper;

    /** 发表评论（需要登录） */
    @Operation(summary = "发表评论", description = "对指定帖子发表评论，支持回复楼层（parentId）")
    @PostMapping("/comments")
    public R<CommentVo> addComment(@Valid @RequestBody CommentCreateDto dto,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return R.success(commentService.addComment(userId, dto));
    }

    /** 查询帖子评论列表 */
    @Operation(summary = "评论列表", description = "获取指定帖子的所有评论（树形结构）")
    @GetMapping("/posts/{postId}/comments")
    public R<List<CommentVo>> listComments(
            @Parameter(description = "帖子 ID") @PathVariable Long postId) {
        return R.success(commentService.listComments(postId));
    }

    /** 删除评论（需要登录，仅本人） */
    @Operation(summary = "删除评论", description = "删除指定评论，仅评论本人可操作")
    @DeleteMapping("/comments/{id}")
    public R<Void> deleteComment(
            @Parameter(description = "评论 ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        commentService.deleteComment(id, userId);
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
