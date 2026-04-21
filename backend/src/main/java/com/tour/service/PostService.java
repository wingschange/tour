package com.tour.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tour.dto.PostCreateDto;
import com.tour.entity.Post;
import com.tour.vo.PostVo;

public interface PostService {

    PostVo createPost(Long userId, PostCreateDto dto);

    PostVo getPost(Long postId, Long currentUserId);

    IPage<PostVo> listPosts(int page, int size, Long sectionId, Long currentUserId);

    IPage<PostVo> listFeedPosts(Long userId, int page, int size);

    void deletePost(Long postId, Long currentUserId);

    IPage<PostVo> listPostsBySection(Long sectionId, int page, int size, Long currentUserId);
}
