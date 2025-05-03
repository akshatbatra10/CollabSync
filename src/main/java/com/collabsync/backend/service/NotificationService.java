package com.collabsync.backend.service;

import com.collabsync.backend.common.dto.notification.NotificationResponseDto;
import com.collabsync.backend.domain.model.Notification;
import com.collabsync.backend.kafka.model.BaseEvent;

import java.util.List;

public interface NotificationService {

    void createNotification(BaseEvent<?> event);

//    List<NotificationResponseDto> getNotificationsByUserId(Integer userId);

    void markNotificationAsRead(Integer notificationId);
}
