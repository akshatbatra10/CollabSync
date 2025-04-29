package com.collabsync.backend.repository;

import com.collabsync.backend.common.dto.notification.NotificationResponseDto;
import com.collabsync.backend.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
