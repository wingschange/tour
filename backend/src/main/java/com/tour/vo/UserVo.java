package com.tour.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息视图对象（对外展示，不含密码）
 */
@Schema(description = "用户资料（对外展示）")
@Data
public class UserVo {

    @Schema(description = "用户 ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像 URL")
    private String avatar;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "角色列表")
    private List<String> roles;

    @Schema(description = "注册时间")
    private LocalDateTime createTime;

    @Schema(description = "粉丝数量")
    private Integer followerCount;

    @Schema(description = "关注数量")
    private Integer followingCount;
}
