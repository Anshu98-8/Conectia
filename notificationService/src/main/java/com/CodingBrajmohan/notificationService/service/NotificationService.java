package com.CodingBrajmohan.notificationService.service;

import com.CodingBrajmohan.notificationService.entity.NotificationEntity;
import com.CodingBrajmohan.notificationService.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void addNotification(NotificationEntity notificationEntity) {
        log.info("Adding notification to db, message: {}", notificationEntity.getMessage());
        notificationEntity = notificationRepository.save(notificationEntity);

//        SendMailer to send email
//        FCM
    }
}
