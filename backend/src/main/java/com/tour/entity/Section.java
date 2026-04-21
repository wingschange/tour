package com.tour.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 板块实体（按省份分类的旅游分区）
 */
@Schema(description = "旅游板块（按省份分类）")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sections")
public class Section {

    @Schema(description = "板块 ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "板块名称")
    private String name;

    @Schema(description = "所属省份")
    private String province;

    @Schema(description = "板块简介")
    private String description;

    @Schema(description = "封面图 URL")
    private String coverImage;

    @Schema(description = "帖子总数（冗余字段，供前端展示）")
    private Integer postCount;

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
