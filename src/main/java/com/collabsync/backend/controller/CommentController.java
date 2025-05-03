package com.collabsync.backend.controller;

import com.collabsync.backend.common.dto.comment.CommentRequestDto;
import com.collabsync.backend.common.dto.comment.CommentResponseDto;
import com.collabsync.backend.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody @Valid CommentRequestDto request) {
        CommentResponseDto comment = commentService.createComment(request);

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping(value = "/task/{taskId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByTaskId(@PathVariable Integer taskId) {
        List<CommentResponseDto> comments = commentService.getCommentsByTaskId(taskId);
        return ResponseEntity.ok(comments);
    }
}
