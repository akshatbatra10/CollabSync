package com.collabsync.backend.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEvent {

    private EventType eventType;
    private LocalDateTime timestamp;
    private Map<String, Object> payload;
}
