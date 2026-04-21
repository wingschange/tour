package com.tour.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentCreateDto {

    @NotNull(message = "Post ID is required")
    private Long postId;

    private Long parentId;

    @NotBlank(message = "Content is required")
    private String content;
}
