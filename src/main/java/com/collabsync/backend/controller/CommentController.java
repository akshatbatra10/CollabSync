package com.collabsync.backend.controller;

import com.collabsync.backend.common.dto.comment.CommentRequestDto;
import com.collabsync.backend.common.dto.comment.CommentResponseDto;
import com.collabsync.backend.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody @Valid CommentRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String createdBy = authentication.getName();

        CommentResponseDto comment = commentService.createComment(request, createdBy);

        return ResponseEntity.ok(comment);
    }

    @GetMapping(value = "/task/{taskId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByTaskId(@PathVariable Integer taskId) {
        List<CommentResponseDto> comments = commentService.getCommentsByTaskId(taskId);
        return ResponseEntity.ok(comments);
    }
}
