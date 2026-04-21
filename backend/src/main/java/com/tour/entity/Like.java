package com.tour.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 点赞记录实体
 */
@Schema(description = "点赞记录")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("likes")
public class Like {

    @Schema(description = "点赞记录 ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "点赞用户 ID")
    private Long userId;

    @Schema(description = "被点赞的帖子 ID")
    private Long postId;

    @Schema(description = "点赞时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
