package com.tour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tour.dto.PostCreateDto;
import com.tour.entity.*;
import com.tour.mapper.*;
import com.tour.moderation.ContentModerationService;
import com.tour.service.PostService;
import com.tour.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private ContentModerationService moderationService;

    @Override
    @Transactional
    public PostVo createPost(Long userId, PostCreateDto dto) {
        if (!moderationService.isContentAllowed(dto.getContent())) {
            throw new IllegalArgumentException("Content is not allowed");
        }

        Post post = Post.builder()
                .userId(userId)
                .title(dto.getTitle())
                .content(dto.getContent())
                .images(dto.getImages())
                .sectionId(dto.getSectionId())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .viewCount(0)
                .likeCount(0)
                .favoriteCount(0)
                .commentCount(0)
                .status(0)
                .build();
        postMapper.insert(post);

        if (dto.getSectionId() != null) {
            Section section = sectionMapper.selectById(dto.getSectionId());
            if (section != null) {
                section.setPostCount(section.getPostCount() == null ? 1 : section.getPostCount() + 1);
                sectionMapper.updateById(section);
            }
        }

        return buildPostVo(post, userId);
    }

    @Override
    @Transactional
    public PostVo getPost(Long postId, Long currentUserId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found");
        }
        post.setViewCount(post.getViewCount() == null ? 1 : post.getViewCount() + 1);
        postMapper.updateById(post);
        return buildPostVo(post, currentUserId);
    }

    @Override
    public IPage<PostVo> listPosts(int page, int size, Long sectionId, Long currentUserId) {
        Page<Post> postPage = new Page<>(page, size);
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<Post>()
                .eq(Post::getStatus, 0)
                .orderByDesc(Post::getCreateTime);
        if (sectionId != null) {
            wrapper.eq(Post::getSectionId, sectionId);
        }
        IPage<Post> result = postMapper.selectPage(postPage, wrapper);
        return result.convert(p -> buildPostVo(p, currentUserId));
    }

    @Override
    public IPage<PostVo> listFeedPosts(Long userId, int page, int size) {
        List<Follow> follows = followMapper.selectList(
                new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId, userId));
        List<Long> followeeIds = follows.stream().map(Follow::getFolloweeId).collect(Collectors.toList());
        followeeIds.add(userId);

        Page<Post> postPage = new Page<>(page, size);
        IPage<Post> result = postMapper.selectPage(postPage,
                new LambdaQueryWrapper<Post>()
                        .in(Post::getUserId, followeeIds)
                        .eq(Post::getStatus, 0)
                        .orderByDesc(Post::getCreateTime));
        return result.convert(p -> buildPostVo(p, userId));
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long currentUserId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found");
        }
        if (!post.getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("No permission to delete this post");
        }
        postMapper.deleteById(postId);
    }

    @Override
    public IPage<PostVo> listPostsBySection(Long sectionId, int page, int size, Long currentUserId) {
        Page<Post> postPage = new Page<>(page, size);
        IPage<Post> result = postMapper.selectPage(postPage,
                new LambdaQueryWrapper<Post>()
                        .eq(Post::getSectionId, sectionId)
                        .eq(Post::getStatus, 0)
                        .orderByDesc(Post::getCreateTime));
        return result.convert(p -> buildPostVo(p, currentUserId));
    }

    private PostVo buildPostVo(Post post, Long currentUserId) {
        PostVo vo = new PostVo();
        vo.setId(post.getId());
        vo.setUserId(post.getUserId());
        vo.setTitle(post.getTitle());
        vo.setContent(post.getContent());
        vo.setImages(post.getImages());
        vo.setSectionId(post.getSectionId());
        vo.setLatitude(post.getLatitude());
        vo.setLongitude(post.getLongitude());
        vo.setViewCount(post.getViewCount());
        vo.setLikeCount(post.getLikeCount());
        vo.setFavoriteCount(post.getFavoriteCount());
        vo.setCommentCount(post.getCommentCount());
        vo.setStatus(post.getStatus());
        vo.setCreateTime(post.getCreateTime());
        vo.setUpdateTime(post.getUpdateTime());

        User author = userMapper.selectById(post.getUserId());
        if (author != null) {
            vo.setAuthorName(author.getUsername());
            vo.setAuthorAvatar(author.getAvatar());
        }

        if (post.getSectionId() != null) {
            Section section = sectionMapper.selectById(post.getSectionId());
            if (section != null) {
                vo.setSectionName(section.getName());
            }
        }

        if (currentUserId != null) {
            boolean liked = likeMapper.selectCount(new LambdaQueryWrapper<Like>()
                    .eq(Like::getUserId, currentUserId)
                    .eq(Like::getPostId, post.getId())) > 0;
            boolean favorited = favoriteMapper.selectCount(new LambdaQueryWrapper<Favorite>()
                    .eq(Favorite::getUserId, currentUserId)
                    .eq(Favorite::getPostId, post.getId())) > 0;
            vo.setLiked(liked);
            vo.setFavorited(favorited);
        }

        return vo;
    }
}
