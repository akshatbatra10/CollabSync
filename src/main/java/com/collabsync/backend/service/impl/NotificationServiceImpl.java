package com.collabsync.backend.service.impl;

import com.collabsync.backend.common.dto.notification.NotificationResponseDto;
import com.collabsync.backend.common.enums.NotificationStatus;
import com.collabsync.backend.domain.model.Notification;
import com.collabsync.backend.kafka.model.*;
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

        switch (event.getEventType()) {
            case COMMENT_CREATED -> {
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

                notificationRepository.save(notification);
                log.info("Notification created for comment event: {}", payload);
            }

            case COLLAB_USER_CHANGED -> {
                CollabUserChangedEvent payload = objectMapper.convertValue(event.getPayload(), CollabUserChangedEvent.class);

                if (payload.getRecipientId() == null) {
                    log.info("No recipient found for collab user change event: {}", payload);
                    return;
                }

                String content = String.format("You were %s to project ID %d as %s",
                        payload.getChangeType(), payload.getProjectId(), payload.getRole());

                Notification notification = Notification.builder()
                        .projectId(payload.getProjectId())
                        .recipientId(payload.getRecipientId())
                        .content(content)
                        .createdBy(payload.getCreatedBy())
                        .status(NotificationStatus.UNREAD)
                        .type(EventType.COLLAB_USER_CHANGED)
                        .build();

                notificationRepository.save(notification);
                log.info("Notification created for collaborator change: {}", payload);
            }

            case PROJECT_UPDATED -> {
                ProjectUpdateEvent payload = objectMapper.convertValue(event.getPayload(), ProjectUpdateEvent.class);

                if (payload.getRecipientId() == null) {
                    log.info("No recipient found for project update event: {}", payload);
                    return;
                }

                String content = String.format("Project ID %d was updated by %s",
                        payload.getProjectId(), payload.getUpdatedBy());

                Notification notification = Notification.builder()
                        .projectId(payload.getProjectId())
                        .recipientId(payload.getRecipientId())
                        .content(content)
                        .createdBy(payload.getUpdatedBy())
                        .status(NotificationStatus.UNREAD)
                        .type(EventType.PROJECT_UPDATED)
                        .build();

                notificationRepository.save(notification);
                log.info("Notification created for project update: {}", payload);
            }

            case PROJECT_DELETED -> {
                ProjectDeleteEvent payload = objectMapper.convertValue(event.getPayload(), ProjectDeleteEvent.class);

                if (payload.getRecipientId() == null) {
                    log.info("No recipient found for project delete event: {}", payload);
                    return;
                }

                String content = String.format("Project ID %d was deleted", payload.getProjectId());

                Notification notification = Notification.builder()
                        .projectId(payload.getProjectId())
                        .recipientId(payload.getRecipientId())
                        .content(content)
                        .createdBy("system")
                        .status(NotificationStatus.UNREAD)
                        .type(EventType.PROJECT_DELETED)
                        .build();

                notificationRepository.save(notification);
                log.info("Notification created for project deletion: {}", payload);
            }

            default -> log.warn("Unhandled event type: {}", event.getEventType());
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
