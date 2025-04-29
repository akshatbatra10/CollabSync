package com.collabsync.backend.service.impl;

import com.collabsync.backend.common.dto.notification.NotificationResponseDto;
import com.collabsync.backend.common.enums.NotificationStatus;
import com.collabsync.backend.domain.model.Notification;
import com.collabsync.backend.kafka.model.BaseEvent;
import com.collabsync.backend.repository.NotificationRepository;
import com.collabsync.backend.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void createNotification(BaseEvent event) {
        Notification notification = Notification.builder()
                .taskId((Integer) event.getPayload().get("taskId"))
                .content((String) event.getPayload().get("content"))
                .createdBy((String) event.getPayload().get("createdBy"))
                .status(NotificationStatus.UNREAD)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        log.info("Saved notification: {}", savedNotification);
    }

//    @Override
//    public List<NotificationResponseDto> getNotificationsByUserId(Integer userId) {
//        return notificationRepository.findByUserId(userId);
//    }

    @Override
    public void markNotificationAsRead(Integer notificationId) {

    }
}
