package com.tour.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户实体
 */
@Schema(description = "用户信息")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class User {

    @Schema(description = "用户 ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户名（唯一）")
    private String username;

    @Schema(description = "密码（BCrypt 加密存储，接口不返回）", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Schema(description = "邮箱（唯一）")
    private String email;

    @Schema(description = "头像 URL")
    private String avatar;

    @Schema(description = "个人简介")
    private String bio;

    /** 角色列表（非数据库字段，查询时填充） */
    @Schema(description = "角色列表，如 [\"ROLE_USER\"]")
    @TableField(exist = false)
    private List<String> roles;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除标志：0=正常，1=已删除 */
    @Schema(description = "逻辑删除：0=正常，1=已删除", accessMode = Schema.AccessMode.READ_ONLY)
    @TableLogic
    private Integer deleted;
}
