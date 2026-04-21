package com.tour.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 收藏记录实体
 */
@Schema(description = "收藏记录")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("favorites")
public class Favorite {

    @Schema(description = "收藏记录 ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "收藏用户 ID")
    private Long userId;

    @Schema(description = "被收藏的帖子 ID")
    private Long postId;

    @Schema(description = "收藏时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
