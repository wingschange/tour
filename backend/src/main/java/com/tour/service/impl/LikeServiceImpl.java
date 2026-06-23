package com.tour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tour.entity.Like;
import com.tour.entity.Post;
import com.tour.mapper.LikeMapper;
import com.tour.mapper.PostMapper;
import com.tour.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 点赞服务实现类
 *
 * <p>提供帖子点赞/取消点赞能力，并同步更新帖子点赞计数。</p>
 */
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private PostMapper postMapper;

    @Override
    @Transactional
    public void like(Long userId, Long postId) {
        boolean alreadyLiked = likeMapper.selectCount(
                new LambdaQueryWrapper<Like>()
                        .eq(Like::getUserId, userId)
                        .eq(Like::getPostId, postId)) > 0;
        if (alreadyLiked) {
            return;
        }
        Like like = Like.builder().userId(userId).postId(postId).build();
        likeMapper.insert(like);

        // 帖子点赞计数 +1
        Post post = postMapper.selectById(postId);
        if (post != null) {
            post.setLikeCount(post.getLikeCount() == null ? 1 : post.getLikeCount() + 1);
            postMapper.updateById(post);
        }
    }

    @Override
    @Transactional
    public void unlike(Long userId, Long postId) {
        int deleted = likeMapper.delete(new LambdaQueryWrapper<Like>()
                .eq(Like::getUserId, userId)
                .eq(Like::getPostId, postId));
        if (deleted > 0) {
            // 帖子点赞计数 -1
            Post post = postMapper.selectById(postId);
            if (post != null && post.getLikeCount() != null && post.getLikeCount() > 0) {
                post.setLikeCount(post.getLikeCount() - 1);
                postMapper.updateById(post);
            }
        }
    }

    @Override
    public boolean isLiked(Long userId, Long postId) {
        return likeMapper.selectCount(new LambdaQueryWrapper<Like>()
                .eq(Like::getUserId, userId)
                .eq(Like::getPostId, postId)) > 0;
    }
}
