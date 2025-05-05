package com.collabsync.backend.kafka.consumer;

import com.collabsync.backend.kafka.model.BaseEvent;
import com.collabsync.backend.kafka.model.CommentCreatedEvent;
import com.collabsync.backend.common.enums.EventType;
import com.collabsync.backend.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "comment-events", groupId = "comment-group")
    public void consumeComment(String message) {
        try {
            BaseEvent<?> eventMessage = objectMapper.readValue(message, BaseEvent.class);

            if (eventMessage.getEventType() == EventType.COMMENT_CREATED) {

                CommentCreatedEvent payload = objectMapper.convertValue(eventMessage.getPayload(), CommentCreatedEvent.class);
                log.info("Received comment event: {} with payload: {}", eventMessage.getEventType(), payload);

                if (payload.getRecipientId() == null) {
                    log.info("No recipient found for comment event: {}", payload);
                    return;
                }

                BaseEvent<CommentCreatedEvent> event = BaseEvent.<CommentCreatedEvent>builder()
                        .eventType(EventType.COMMENT_CREATED)
                        .timestamp(LocalDateTime.now())
                        .payload(payload)
                        .build();

                notificationService.createNotification(event);
            }
        } catch (Exception e) {
            log.error("Error processing comment event", e);
        }
    }
}
