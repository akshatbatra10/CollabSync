package com.collabsync.backend.kafka.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskChangeEvent {
    private Integer taskId;
    private Integer recipientId;
    private String updatedBy;
}
