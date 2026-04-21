package com.tour.controller;

import com.tour.common.R;
import com.tour.dto.CommentCreateDto;
import com.tour.entity.User;
import com.tour.mapper.UserMapper;
import com.tour.service.CommentService;
import com.tour.vo.CommentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Comment", description = "Comment endpoints")
@RestController
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserMapper userMapper;

    @Operation(summary = "Add a comment")
    @PostMapping("/comments")
    public R<CommentVo> addComment(@Valid @RequestBody CommentCreateDto dto,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return R.success(commentService.addComment(userId, dto));
    }

    @Operation(summary = "List comments for a post")
    @GetMapping("/posts/{postId}/comments")
    public R<List<CommentVo>> listComments(@PathVariable Long postId) {
        return R.success(commentService.listComments(postId));
    }

    @Operation(summary = "Delete a comment")
    @DeleteMapping("/comments/{id}")
    public R<Void> deleteComment(@PathVariable Long id,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        commentService.deleteComment(id, userId);
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
