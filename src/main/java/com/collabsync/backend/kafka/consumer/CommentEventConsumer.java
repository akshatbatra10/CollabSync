package com.collabsync.backend.kafka.consumer;

import com.collabsync.backend.kafka.model.EventMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "comment-events", groupId = "collabsync-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            EventMessage eventMessage = objectMapper.readValue(record.value(), EventMessage.class);
            log.info("Received comment event: {}", eventMessage);
        } catch (Exception e) {
            log.error("Error processing comment event", e);
        }
    }
}
