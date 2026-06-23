package com.tour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tour.entity.Favorite;
import com.tour.entity.Post;
import com.tour.mapper.FavoriteMapper;
import com.tour.mapper.PostMapper;
import com.tour.service.FavoriteService;
import com.tour.service.PostService;
import com.tour.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏服务实现类
 *
 * <p>提供帖子收藏/取消收藏能力，并同步更新帖子收藏计数。</p>
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostService postService;

    @Override
    @Transactional
    public void favorite(Long userId, Long postId) {
        boolean alreadyFavorited = favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .eq(Favorite::getPostId, postId)) > 0;
        if (alreadyFavorited) {
            return;
        }
        Favorite favorite = Favorite.builder().userId(userId).postId(postId).build();
        favoriteMapper.insert(favorite);

        // 帖子收藏计数 +1
        Post post = postMapper.selectById(postId);
        if (post != null) {
            post.setFavoriteCount(post.getFavoriteCount() == null ? 1 : post.getFavoriteCount() + 1);
            postMapper.updateById(post);
        }
    }

    @Override
    @Transactional
    public void unfavorite(Long userId, Long postId) {
        int deleted = favoriteMapper.delete(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getPostId, postId));
        if (deleted > 0) {
            // 帖子收藏计数 -1
            Post post = postMapper.selectById(postId);
            if (post != null && post.getFavoriteCount() != null && post.getFavoriteCount() > 0) {
                post.setFavoriteCount(post.getFavoriteCount() - 1);
                postMapper.updateById(post);
            }
        }
    }

    @Override
    public boolean isFavorited(Long userId, Long postId) {
        return favoriteMapper.selectCount(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getPostId, postId)) > 0;
    }

    @Override
    public IPage<PostVo> listFavorites(Long userId, int page, int size) {
        Page<Favorite> favPage = new Page<>(page, size);
        IPage<Favorite> favResult = favoriteMapper.selectPage(favPage,
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .orderByDesc(Favorite::getCreateTime));

        List<PostVo> postVos = favResult.getRecords().stream()
                .map(f -> {
                    Post post = postMapper.selectById(f.getPostId());
                    if (post == null) return null;
                    return postService.getPost(post.getId(), userId);
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());

        Page<PostVo> resultPage = new Page<>(favResult.getCurrent(), favResult.getSize(), favResult.getTotal());
        resultPage.setRecords(postVos);
        return resultPage;
    }
}
