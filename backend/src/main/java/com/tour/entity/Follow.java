package com.tour.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 关注关系实体
 */
@Schema(description = "用户关注关系")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("follows")
public class Follow {

    @Schema(description = "关注关系 ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "关注者（粉丝）用户 ID")
    private Long followerId;

    @Schema(description = "被关注者用户 ID")
    private Long followeeId;

    @Schema(description = "关注时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
