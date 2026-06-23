package com.tour.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 用户角色关联实体
 *
 * <p>维护用户与角色之间的多对多关系，用于 RBAC 权限控制。</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_roles")
public class UserRole {

    /** 关联记录 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户 ID */
    private Long userId;

    /** 角色 ID */
    private Long roleId;

    /** 关联创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
