package com.tour.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tour.vo.PostVo;

public interface FavoriteService {

    void favorite(Long userId, Long postId);

    void unfavorite(Long userId, Long postId);

    boolean isFavorited(Long userId, Long postId);

    IPage<PostVo> listFavorites(Long userId, int page, int size);
}
