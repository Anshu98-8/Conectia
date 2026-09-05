package com.CodingBrajmohan.notificationService.repository;

import com.CodingBrajmohan.notificationService.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
