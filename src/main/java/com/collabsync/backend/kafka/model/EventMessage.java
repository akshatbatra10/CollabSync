package com.collabsync.backend.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventMessage {
    private String eventType;
    private String entityId;
    private String entityType;
    private String actor;
    private LocalDateTime createdAt;
    private String message;
}
