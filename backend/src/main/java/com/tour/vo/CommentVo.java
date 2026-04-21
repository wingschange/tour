package com.tour.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentVo {

    private Long id;
    private Long postId;
    private Long userId;
    private String username;
    private String userAvatar;
    private Long parentId;
    private String content;
    private LocalDateTime createTime;
    private List<CommentVo> children;
}
