package com.tour.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 评论实体
 */
@Schema(description = "帖子评论")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("comments")
public class Comment {

    @Schema(description = "评论 ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "所属帖子 ID")
    private Long postId;

    @Schema(description = "评论用户 ID")
    private Long userId;

    /** 回复的父评论 ID，顶级评论为 null */
    @Schema(description = "父评论 ID，顶级评论为 null")
    private Long parentId;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除：0=正常，1=已删除", accessMode = Schema.AccessMode.READ_ONLY)
    @TableLogic
    private Integer deleted;
}
