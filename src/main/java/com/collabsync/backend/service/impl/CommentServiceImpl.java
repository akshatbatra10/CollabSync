package com.collabsync.backend.service.impl;

import com.collabsync.backend.common.dto.comment.CommentRequestDto;
import com.collabsync.backend.common.dto.comment.CommentResponseDto;
import com.collabsync.backend.common.exceptions.ResourceNotFoundException;
import com.collabsync.backend.domain.model.Comment;
import com.collabsync.backend.domain.model.Task;
import com.collabsync.backend.kafka.model.BaseEvent;
import com.collabsync.backend.kafka.model.EventType;
import com.collabsync.backend.kafka.producer.EventPublisher;
import com.collabsync.backend.repository.CommentRepository;
import com.collabsync.backend.repository.TaskRepository;
import com.collabsync.backend.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto request) {
        String createdBy = SecurityContextHolder.getContext().getAuthentication().getName();
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + request.getTaskId()));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .createdBy(createdBy)
                .task(task)
                .build();

        Comment savedComment = commentRepository.save(comment);

        Map<String, Object> payload = Map.of("commentId", savedComment.getId(),
                "taskId", savedComment.getTask().getId(),
                "createdBy", savedComment.getCreatedBy(),
                "content", savedComment.getContent());

        BaseEvent baseEvent = BaseEvent.builder()
                .eventType(EventType.COMMENT_CREATED)
                .timestamp(LocalDateTime.now())
                .payload(payload)
                .build();

        eventPublisher.publish("comment-events", baseEvent);

        return mapToDto(savedComment);
    }

    @Override
    public List<CommentResponseDto> getCommentsByTaskId(Integer taskId) {
        List<Comment> comments = commentRepository.findByTaskId(taskId);
        return comments.stream()
                .map(this::mapToDto)
                .toList();
    }


    private CommentResponseDto mapToDto(Comment comment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdBy(comment.getCreatedBy())
                .taskId(comment.getTask().getId())
                .createdAt(comment.getCreatedAt().format(formatter))
                .updatedAt(comment.getUpdatedAt() != null ? comment.getUpdatedAt().format(formatter) : null)
                .build();
    }
}
