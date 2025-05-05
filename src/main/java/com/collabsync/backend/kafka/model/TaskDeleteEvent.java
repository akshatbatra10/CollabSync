package com.collabsync.backend.kafka.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDeleteEvent {
    private Integer taskId;
    private Integer recipientId;
}
