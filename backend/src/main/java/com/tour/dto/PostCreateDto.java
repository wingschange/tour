package com.tour.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 发布帖子请求 DTO
 */
@Schema(description = "发布帖子请求参数")
@Data
public class PostCreateDto {

    @Schema(description = "标题", example = "西藏自驾游攻略", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "正文内容", example = "这次旅行真的太棒了……", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "内容不能为空")
    private String content;

    @Schema(description = "图片 URL，多张以英文逗号分隔", example = "http://example.com/img1.jpg,http://example.com/img2.jpg")
    private String images;

    @Schema(description = "所属板块 ID", example = "1")
    private Long sectionId;

    @Schema(description = "纬度（高德坐标系）", example = "29.6489")
    private Double latitude;

    @Schema(description = "经度（高德坐标系）", example = "91.1172")
    private Double longitude;
}
