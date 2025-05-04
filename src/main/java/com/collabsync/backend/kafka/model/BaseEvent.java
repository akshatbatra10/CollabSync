package com.collabsync.backend.kafka.model;

import com.collabsync.backend.kafka.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEvent<T> {

    private EventType eventType;
    private LocalDateTime timestamp;
    private T payload;
}
