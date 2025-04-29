package com.collabsync.backend.kafka.consumer;

import com.collabsync.backend.common.enums.NotificationStatus;
import com.collabsync.backend.domain.model.Notification;
import com.collabsync.backend.kafka.model.BaseEvent;
import com.collabsync.backend.kafka.model.EventType;
import com.collabsync.backend.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "comment-events", groupId = "comment-group")
    public void consume(String message) {
        try {
            BaseEvent eventMessage = objectMapper.readValue(message, BaseEvent.class);
            log.info("Received comment event: {} with payload: {}", eventMessage.getEventType(), eventMessage.getPayload());

            notificationService.createNotification(eventMessage);
        } catch (Exception e) {
            log.error("Error processing comment event", e);
        }
    }
}
