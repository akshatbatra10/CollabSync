package com.collabsync.backend.service.impl;

import com.collabsync.backend.common.enums.NotificationStatus;
import com.collabsync.backend.domain.model.Notification;
import com.collabsync.backend.kafka.model.BaseEvent;
import com.collabsync.backend.kafka.model.CommentCreatedEvent;
import com.collabsync.backend.common.enums.EventType;
import com.collabsync.backend.repository.NotificationRepository;
import com.collabsync.backend.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void createNotification(BaseEvent<?> event) {

        if (event.getEventType() == EventType.COMMENT_CREATED) {
            CommentCreatedEvent payload = objectMapper.convertValue(event.getPayload(), CommentCreatedEvent.class);

            if (payload.getRecipientId() == null) {
                log.info("No recipient found for comment event: {}", payload);
                return;
            }

            Notification notification = Notification.builder()
                    .taskId(payload.getTaskId())
                    .recipientId(payload.getRecipientId())
                    .content(payload.getContent())
                    .createdBy(payload.getCreatedBy())
                    .status(NotificationStatus.UNREAD)
                    .type(EventType.COMMENT_CREATED)
                    .build();

            Notification savedNotification = notificationRepository.save(notification);
            log.info("Saved notification: {}", savedNotification);
        }

    }

//    @Override
//    public List<NotificationResponseDto> getNotificationsByUserId(Integer userId) {
//        return notificationRepository.findByUserId(userId);
//    }

    @Override
    public void markNotificationAsRead(Integer notificationId) {

    }
}
