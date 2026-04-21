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
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Section", description = "Section endpoints")
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

    @Operation(summary = "List all sections")
    @GetMapping
    public R<IPage<Section>> listSections(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        return R.success(sectionService.listSections(page, size));
    }

    @Operation(summary = "Get section by ID")
    @GetMapping("/{id}")
    public R<Section> getSection(@PathVariable Long id) {
        return R.success(sectionService.getSection(id));
    }

    @Operation(summary = "List posts by section")
    @GetMapping("/{id}/posts")
    public R<IPage<PostVo>> listPostsBySection(@PathVariable Long id,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = userDetails != null ? getUserId(userDetails) : null;
        return R.success(postService.listPostsBySection(id, page, size, currentUserId));
    }

    private Long getUserId(UserDetails userDetails) {
        User user = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                        .eq(User::getUsername, userDetails.getUsername()));
        if (user == null) throw new IllegalArgumentException("User not found");
        return user.getId();
    }
}
