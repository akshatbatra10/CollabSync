package com.collabsync.backend.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class EventMessage {
    private String eventType;
    private Integer entityId;
    private String createdBy;
    private LocalDateTime createdAt;
}
