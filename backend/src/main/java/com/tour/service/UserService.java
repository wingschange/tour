package com.tour.service;

import com.tour.dto.LoginDto;
import com.tour.dto.RegisterDto;
import com.tour.entity.User;
import com.tour.vo.UserVo;

import java.util.List;

public interface UserService {

    void register(RegisterDto registerDto);

    String login(LoginDto loginDto);

    UserVo getProfile(Long userId);

    void updateProfile(Long userId, User user);

    void follow(Long followerId, Long followeeId);

    void unfollow(Long followerId, Long followeeId);

    List<UserVo> getFollowers(Long userId);

    List<UserVo> getFollowing(Long userId);
}
