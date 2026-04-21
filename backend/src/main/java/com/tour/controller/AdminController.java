package com.tour.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tour.common.R;
import com.tour.entity.Post;
import com.tour.entity.Section;
import com.tour.service.AdminService;
import com.tour.service.SectionService;
import com.tour.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Admin", description = "Admin management endpoints")
@RestController
@RequestMapping("/admin")
@CrossOrigin
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SectionService sectionService;

    @Operation(summary = "List all users")
    @GetMapping("/users")
    public R<IPage<UserVo>> listUsers(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return R.success(adminService.listUsers(page, size));
    }

    @Operation(summary = "Delete a user")
    @DeleteMapping("/users/{id}")
    public R<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return R.success();
    }

    @Operation(summary = "Update user role")
    @PutMapping("/users/{id}/role")
    public R<Void> updateUserRole(@PathVariable Long id,
                                   @RequestBody Map<String, String> body) {
        String roleName = body.get("role");
        if (roleName == null || roleName.isEmpty()) {
            throw new IllegalArgumentException("Role name is required");
        }
        adminService.updateUserRole(id, roleName);
        return R.success();
    }

    @Operation(summary = "List all posts")
    @GetMapping("/posts")
    public R<IPage<Post>> listAllPosts(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return R.success(adminService.listAllPosts(page, size));
    }

    @Operation(summary = "Delete a post (admin)")
    @DeleteMapping("/posts/{id}")
    public R<Void> deletePost(@PathVariable Long id) {
        adminService.deletePost(id);
        return R.success();
    }

    @Operation(summary = "List all sections (admin)")
    @GetMapping("/sections")
    public R<IPage<Section>> listSections(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        return R.success(adminService.listSections(page, size));
    }

    @Operation(summary = "Create a section (admin)")
    @PostMapping("/sections")
    public R<Section> createSection(@RequestBody Section section) {
        return R.success(sectionService.createSection(section));
    }

    @Operation(summary = "Update a section (admin)")
    @PutMapping("/sections/{id}")
    public R<Section> updateSection(@PathVariable Long id, @RequestBody Section section) {
        return R.success(sectionService.updateSection(id, section));
    }

    @Operation(summary = "Delete a section (admin)")
    @DeleteMapping("/sections/{id}")
    public R<Void> deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
        return R.success();
    }
}
