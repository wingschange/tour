package com.tour.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tour.common.R;
import com.tour.entity.Section;
import com.tour.entity.User;
import com.tour.mapper.UserMapper;
import com.tour.service.PostService;
import com.tour.service.SectionService;
import com.tour.vo.PostVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 板块控制器 —— 板块列表、板块帖子
 */
@Tag(name = "板块", description = "旅游板块（按省份分类）查询及板块内帖子列表接口")
@RestController
@RequestMapping("/sections")
@CrossOrigin
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserMapper userMapper;

    /** 获取板块列表（公开接口） */
    @Operation(summary = "板块列表", description = "分页查询所有板块，无需登录")
    @GetMapping
    public R<IPage<Section>> listSections(
            @Parameter(description = "页码，默认 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页条数，默认 20") @RequestParam(defaultValue = "20") int size) {
        return R.success(sectionService.listSections(page, size));
    }

    /** 获取板块详情（公开接口） */
    @Operation(summary = "板块详情", description = "根据板块 ID 获取详细信息，无需登录")
    @GetMapping("/{id}")
    public R<Section> getSection(
            @Parameter(description = "板块 ID") @PathVariable Long id) {
        return R.success(sectionService.getSection(id));
    }

    /** 获取板块内帖子列表（公开接口） */
    @Operation(summary = "板块帖子", description = "获取指定板块下的帖子，分页返回，无需登录")
    @GetMapping("/{id}/posts")
    public R<IPage<PostVo>> listPostsBySection(
            @Parameter(description = "板块 ID") @PathVariable Long id,
            @Parameter(description = "页码，默认 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页条数，默认 10") @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = userDetails != null ? getUserId(userDetails) : null;
        return R.success(postService.listPostsBySection(id, page, size, currentUserId));
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
