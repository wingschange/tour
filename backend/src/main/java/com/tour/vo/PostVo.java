package com.tour.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 帖子视图对象（含作者信息、互动状态）
 */
@Schema(description = "帖子详情（含作者信息与当前用户互动状态）")
@Data
public class PostVo {

    @Schema(description = "帖子 ID")
    private Long id;

    @Schema(description = "作者用户 ID")
    private Long userId;

    @Schema(description = "作者用户名")
    private String authorName;

    @Schema(description = "作者头像 URL")
    private String authorAvatar;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "正文内容")
    private String content;

    @Schema(description = "图片 URL，多张以英文逗号分隔")
    private String images;

    @Schema(description = "所属板块 ID")
    private Long sectionId;

    @Schema(description = "所属板块名称")
    private String sectionName;

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

    @Schema(description = "状态：0=正常，1=被版主删除")
    private Integer status;

    @Schema(description = "发布时间")
    private LocalDateTime createTime;

    @Schema(description = "最后更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "当前用户是否已点赞（未登录时为 false）")
    private Boolean liked;

    @Schema(description = "当前用户是否已收藏（未登录时为 false）")
    private Boolean favorited;
}
