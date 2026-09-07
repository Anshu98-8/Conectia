package com.CodingBrajmohan.notificationService.consumer;

import com.CodingBrajmohan.notificationService.entity.NotificationEntity;
import com.CodingBrajmohan.notificationService.service.NotificationService;
import connectionService.event.ConnectionAcceptedEvent;
import connectionService.event.ConnectionRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "connection_requested_topic")
    public void handleConnectionRequestedEvent(ConnectionRequestedEvent connectionRequestedEvent) {
        log.info("handleConnectionRequestedEvent: {}", connectionRequestedEvent);

        String message = String.format(
                "User with id: %d sent you a connection request",
                connectionRequestedEvent.getSenderId()
        );

        NotificationEntity notificationEntity = NotificationEntity.builder()
                .message(message)
                .userId(connectionRequestedEvent.getReceiverId())
                .build();

        notificationService.addNotification(notificationEntity);
    }

    @KafkaListener(topics = "connection_accepted_topic")
    public void handleConnectionAcceptedEvent(ConnectionAcceptedEvent connectionAcceptedEvent) {
        log.info("handleConnectionAcceptedEvent: {}", connectionAcceptedEvent);

        String message = String.format(
                "User with id: %d has accepted your connection request",
                connectionAcceptedEvent.getReceiverId()
        );

        NotificationEntity notificationEntity = NotificationEntity.builder()
                .message(message)
                .userId(connectionAcceptedEvent.getSenderId())
                .build();

        notificationService.addNotification(notificationEntity);
    }
}
