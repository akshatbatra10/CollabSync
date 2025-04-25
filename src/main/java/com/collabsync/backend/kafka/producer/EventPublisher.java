package com.collabsync.backend.kafka.producer;

import com.collabsync.backend.kafka.model.EventMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publish(String topic, EventMessage eventMessage) {
        try {
            String message = objectMapper.writeValueAsString(eventMessage);
            kafkaTemplate.send(topic, message);
            log.info("Publishing message: {}", message);
        } catch (JsonProcessingException e) {
            log.error("Error publishing message", e);
        }
    }
}
