package com.tour.service;

import com.tour.dto.CommentCreateDto;
import com.tour.vo.CommentVo;

import java.util.List;

/**
 * 评论服务接口 —— 发表评论、查询评论、删除评论
 */
public interface CommentService {

    /**
     * 发表评论
     *
     * @param userId 评论用户 ID
     * @param dto    评论内容（含帖子 ID 与父评论 ID）
     * @return 评论视图对象
     */
    CommentVo addComment(Long userId, CommentCreateDto dto);

    /**
     * 查询指定帖子的评论列表（树形结构）
     *
     * @param postId 帖子 ID
     * @return 顶级评论列表，每个评论包含子评论
     */
    List<CommentVo> listComments(Long postId);

    /**
     * 删除评论（仅评论本人可操作）
     *
     * @param commentId 评论 ID
     * @param userId    当前用户 ID
     */
    void deleteComment(Long commentId, Long userId);
}
