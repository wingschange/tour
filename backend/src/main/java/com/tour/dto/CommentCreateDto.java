package com.tour.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 发表评论请求 DTO
 */
@Schema(description = "发表评论请求参数")
@Data
public class CommentCreateDto {

    @Schema(description = "帖子 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "帖子 ID 不能为空")
    private Long postId;

    @Schema(description = "父评论 ID，回复顶级评论时传 null", example = "null")
    private Long parentId;

    @Schema(description = "评论内容", example = "写得很棒！", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "评论内容不能为空")
    private String content;
}
