package com.collabsync.backend.service;

import com.collabsync.backend.common.dto.comment.CommentRequestDto;
import com.collabsync.backend.common.dto.comment.CommentResponseDto;

import java.util.List;

public interface CommentService {

    CommentResponseDto createComment(CommentRequestDto comment, String createdBy);

    List<CommentResponseDto> getCommentsByTaskId(Integer taskId);
}
