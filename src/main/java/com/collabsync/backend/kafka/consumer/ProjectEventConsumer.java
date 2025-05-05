package com.collabsync.backend.kafka.consumer;

import com.collabsync.backend.kafka.model.BaseEvent;
import com.collabsync.backend.common.enums.EventType;
import com.collabsync.backend.kafka.model.CollabUserChangedEvent;
import com.collabsync.backend.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectEventConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "project-events", groupId = "project-group")
    public void consumeProjectEvent(String message) {
        try {
            BaseEvent<?> eventMessage = objectMapper.readValue(message, BaseEvent.class);

            if (eventMessage.getEventType() == EventType.COLLAB_USER_CHANGED) {
                CollabUserChangedEvent payload = objectMapper.convertValue(eventMessage.getPayload(), CollabUserChangedEvent.class);
                log.info("Received project event with payload: {}", payload);

                if (payload.getRecipientId() == null) {
                    log.info("No recipient found for project event: {}", payload);
                    return;
                }

                BaseEvent<CollabUserChangedEvent> event = BaseEvent.<CollabUserChangedEvent>builder()
                        .eventType(EventType.COLLAB_USER_CHANGED)
                        .timestamp(eventMessage.getTimestamp())
                        .payload(payload)
                        .build();

                notificationService.createNotification(event);
            } else {
                log.info("Received unsupported project event: {}", eventMessage.getEventType());
            }
        } catch (Exception e) {
            log.error("Error processing project event", e);
        }
    }
}
