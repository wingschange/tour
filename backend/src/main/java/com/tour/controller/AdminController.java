package com.tour.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tour.common.R;
import com.tour.entity.Post;
import com.tour.entity.Section;
import com.tour.service.AdminService;
import com.tour.service.SectionService;
import com.tour.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理员控制器 —— 用户管理、帖子管理、板块管理
 * <p>所有接口均需要 ROLE_ADMIN 权限</p>
 */
@Tag(name = "管理员", description = "后台管理接口，需要 ROLE_ADMIN 权限")
@RestController
@RequestMapping("/admin")
@CrossOrigin
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SectionService sectionService;

    // ===================== 用户管理 =====================

    /** 分页查询所有用户 */
    @Operation(summary = "用户列表", description = "分页获取所有注册用户")
    @GetMapping("/users")
    public R<IPage<UserVo>> listUsers(
            @Parameter(description = "页码，默认 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页条数，默认 10") @RequestParam(defaultValue = "10") int size) {
        return R.success(adminService.listUsers(page, size));
    }

    /** 删除指定用户（逻辑删除） */
    @Operation(summary = "删除用户", description = "逻辑删除指定用户，用户数据不会物理删除")
    @DeleteMapping("/users/{id}")
    public R<Void> deleteUser(@Parameter(description = "用户 ID") @PathVariable Long id) {
        adminService.deleteUser(id);
        return R.success();
    }

    /** 修改用户角色 */
    @Operation(summary = "修改用户角色", description = "修改指定用户的角色，body 格式：{\"role\": \"ROLE_MODERATOR\"}")
    @PutMapping("/users/{id}/role")
    public R<Void> updateUserRole(
            @Parameter(description = "用户 ID") @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String roleName = body.get("role");
        if (roleName == null || roleName.isEmpty()) {
            throw new IllegalArgumentException("角色名称不能为空");
        }
        adminService.updateUserRole(id, roleName);
        return R.success();
    }

    // ===================== 帖子管理 =====================

    /** 分页查询所有帖子 */
    @Operation(summary = "帖子列表（管理）", description = "分页获取所有帖子，包含已删除的")
    @GetMapping("/posts")
    public R<IPage<Post>> listAllPosts(
            @Parameter(description = "页码，默认 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页条数，默认 10") @RequestParam(defaultValue = "10") int size) {
        return R.success(adminService.listAllPosts(page, size));
    }

    /** 管理员删除帖子 */
    @Operation(summary = "删除帖子（管理）", description = "管理员强制删除指定帖子")
    @DeleteMapping("/posts/{id}")
    public R<Void> deletePost(@Parameter(description = "帖子 ID") @PathVariable Long id) {
        adminService.deletePost(id);
        return R.success();
    }

    // ===================== 板块管理 =====================

    /** 分页查询所有板块 */
    @Operation(summary = "板块列表（管理）", description = "管理员分页查询所有板块")
    @GetMapping("/sections")
    public R<IPage<Section>> listSections(
            @Parameter(description = "页码，默认 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页条数，默认 20") @RequestParam(defaultValue = "20") int size) {
        return R.success(adminService.listSections(page, size));
    }

    /** 新建板块 */
    @Operation(summary = "新建板块", description = "管理员创建新的旅游板块")
    @PostMapping("/sections")
    public R<Section> createSection(@RequestBody Section section) {
        return R.success(sectionService.createSection(section));
    }

    /** 修改板块信息 */
    @Operation(summary = "修改板块", description = "管理员修改板块信息")
    @PutMapping("/sections/{id}")
    public R<Section> updateSection(
            @Parameter(description = "板块 ID") @PathVariable Long id,
            @RequestBody Section section) {
        return R.success(sectionService.updateSection(id, section));
    }

    /** 删除板块 */
    @Operation(summary = "删除板块", description = "管理员删除指定板块（逻辑删除）")
    @DeleteMapping("/sections/{id}")
    public R<Void> deleteSection(@Parameter(description = "板块 ID") @PathVariable Long id) {
        sectionService.deleteSection(id);
        return R.success();
    }
}
