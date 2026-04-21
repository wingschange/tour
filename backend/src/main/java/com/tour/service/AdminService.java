package com.tour.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tour.entity.Post;
import com.tour.entity.Section;
import com.tour.entity.User;
import com.tour.vo.UserVo;

public interface AdminService {

    IPage<UserVo> listUsers(int page, int size);

    void deleteUser(Long userId);

    void updateUserRole(Long userId, String roleName);

    IPage<Post> listAllPosts(int page, int size);

    void deletePost(Long postId);

    IPage<Section> listSections(int page, int size);
}
