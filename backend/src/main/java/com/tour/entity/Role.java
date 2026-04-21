package com.tour.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 角色实体（RBAC 权限控制）
 * <p>内置角色：ROLE_USER / ROLE_MODERATOR / ROLE_ADMIN</p>
 */
@Schema(description = "系统角色")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("roles")
public class Role {

    @Schema(description = "角色 ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "角色名称，如 ROLE_USER / ROLE_MODERATOR / ROLE_ADMIN")
    private String name;

    @Schema(description = "角色描述")
    private String description;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
