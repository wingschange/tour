package com.tour.service;

import com.tour.dto.CommentCreateDto;
import com.tour.vo.CommentVo;

import java.util.List;

public interface CommentService {

    CommentVo addComment(Long userId, CommentCreateDto dto);

    List<CommentVo> listComments(Long postId);

    void deleteComment(Long commentId, Long userId);
}
