package com.tour.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostVo {

    private Long id;
    private Long userId;
    private String authorName;
    private String authorAvatar;
    private String title;
    private String content;
    private String images;
    private Long sectionId;
    private String sectionName;
    private Double latitude;
    private Double longitude;
    private Integer viewCount;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer commentCount;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean liked;
    private Boolean favorited;
}
