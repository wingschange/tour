package com.tour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tour.dto.CommentCreateDto;
import com.tour.entity.Comment;
import com.tour.entity.Post;
import com.tour.entity.User;
import com.tour.mapper.CommentMapper;
import com.tour.mapper.PostMapper;
import com.tour.mapper.UserMapper;
import com.tour.service.CommentService;
import com.tour.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评论服务实现类
 *
 * <p>支持发表评论、按帖子查询树形评论、删除评论，并同步维护帖子评论计数。</p>
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public CommentVo addComment(Long userId, CommentCreateDto dto) {
        Post post = postMapper.selectById(dto.getPostId());
        if (post == null) {
            throw new IllegalArgumentException("帖子不存在");
        }

        Comment comment = Comment.builder()
                .postId(dto.getPostId())
                .userId(userId)
                .parentId(dto.getParentId())
                .content(dto.getContent())
                .build();
        commentMapper.insert(comment);

        // 帖子评论计数 +1
        post.setCommentCount(post.getCommentCount() == null ? 1 : post.getCommentCount() + 1);
        postMapper.updateById(post);

        return buildCommentVo(comment);
    }

    @Override
    public List<CommentVo> listComments(Long postId) {
        // 查询该帖子下的全部评论，按时间正序
        List<Comment> allComments = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getPostId, postId)
                        .orderByAsc(Comment::getCreateTime));

        // 将非顶级评论按 parentId 分组
        Map<Long, List<CommentVo>> childrenMap = allComments.stream()
                .filter(c -> c.getParentId() != null)
                .map(this::buildCommentVo)
                .collect(Collectors.groupingBy(CommentVo::getParentId));

        // 提取顶级评论并挂载子评论
        List<CommentVo> roots = allComments.stream()
                .filter(c -> c.getParentId() == null)
                .map(this::buildCommentVo)
                .collect(Collectors.toList());

        roots.forEach(root -> root.setChildren(childrenMap.getOrDefault(root.getId(), new ArrayList<>())));

        return roots;
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("无权删除该评论");
        }

        // 帖子评论计数 -1
        Post post = postMapper.selectById(comment.getPostId());
        if (post != null && post.getCommentCount() != null && post.getCommentCount() > 0) {
            post.setCommentCount(post.getCommentCount() - 1);
            postMapper.updateById(post);
        }

        commentMapper.deleteById(commentId);
    }

    /**
     * 将 Comment 实体转换为含作者信息的视图对象
     *
     * @param comment 评论实体
     * @return 评论视图对象
     */
    private CommentVo buildCommentVo(Comment comment) {
        CommentVo vo = new CommentVo();
        vo.setId(comment.getId());
        vo.setPostId(comment.getPostId());
        vo.setUserId(comment.getUserId());
        vo.setParentId(comment.getParentId());
        vo.setContent(comment.getContent());
        vo.setCreateTime(comment.getCreateTime());

        User user = userMapper.selectById(comment.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
            vo.setUserAvatar(user.getAvatar());
        }

        return vo;
    }
}
