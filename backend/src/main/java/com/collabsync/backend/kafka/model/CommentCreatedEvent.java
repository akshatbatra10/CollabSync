package com.collabsync.backend.kafka.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreatedEvent {
    private Integer commentId;
    private Integer taskId;
    private Integer projectId;
    private String content;
    private String createdBy;
    private Integer recipientId;
}
