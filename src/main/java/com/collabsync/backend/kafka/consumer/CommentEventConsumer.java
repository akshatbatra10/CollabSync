package com.collabsync.backend.kafka.consumer;

import com.collabsync.backend.kafka.model.BaseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "comment-events", groupId = "comment-group")
    public void consume(String message) {
        try {
            BaseEvent eventMessage = objectMapper.readValue(message, BaseEvent.class);
            log.info("Received comment event: {} with payload: {}", eventMessage.getEventType(), eventMessage.getPayload());
        } catch (Exception e) {
            log.error("Error processing comment event", e);
        }
    }
}
