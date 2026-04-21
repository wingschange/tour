package com.tour.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 帖子实体
 */
@Schema(description = "旅游帖子")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("posts")
public class Post {

    @Schema(description = "帖子 ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "发布用户 ID")
    private Long userId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "正文内容")
    private String content;

    /** 逗号分隔的图片 URL 列表 */
    @Schema(description = "图片 URL，多个以英文逗号分隔")
    private String images;

    @Schema(description = "所属板块 ID")
    private Long sectionId;

    @Schema(description = "纬度（高德坐标系）")
    private Double latitude;

    @Schema(description = "经度（高德坐标系）")
    private Double longitude;

    @Schema(description = "浏览次数")
    private Integer viewCount;

    @Schema(description = "点赞次数")
    private Integer likeCount;

    @Schema(description = "收藏次数")
    private Integer favoriteCount;

    @Schema(description = "评论次数")
    private Integer commentCount;

    /** 帖子状态：0=正常，1=被版主删除 */
    @Schema(description = "状态：0=正常，1=被版主删除")
    private Integer status;

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
