package com.tour.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论视图对象（树形结构，含子评论）
 */
@Schema(description = "评论详情（含作者信息与子评论）")
@Data
public class CommentVo {

    @Schema(description = "评论 ID")
    private Long id;

    @Schema(description = "所属帖子 ID")
    private Long postId;

    @Schema(description = "评论用户 ID")
    private Long userId;

    @Schema(description = "评论用户名")
    private String username;

    @Schema(description = "评论用户头像 URL")
    private String userAvatar;

    @Schema(description = "父评论 ID，顶级评论为 null")
    private Long parentId;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "评论时间")
    private LocalDateTime createTime;

    @Schema(description = "子评论列表（回复）")
    private List<CommentVo> children;
}
