package com.tour.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserVo {

    private Long id;
    private String username;
    private String email;
    private String avatar;
    private String bio;
    private List<String> roles;
    private LocalDateTime createTime;
    private Integer followerCount;
    private Integer followingCount;
}
