package com.CodingBrajmohan.connectionService.service;

import com.CodingBrajmohan.connectionService.auth.AuthContextHolder;
import com.CodingBrajmohan.connectionService.entity.PersonEntity;
import com.CodingBrajmohan.connectionService.event.ConnectionAcceptedEvent;
import com.CodingBrajmohan.connectionService.event.ConnectionRequestedEvent;
import com.CodingBrajmohan.connectionService.exception.BadRequestException;
import com.CodingBrajmohan.connectionService.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsService {

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, ConnectionRequestedEvent> connectionRequestedEventKafkaTemplate;
    private final KafkaTemplate<Long, ConnectionAcceptedEvent> connectionAcceptedEventKafkaTemplate;

    public List<PersonEntity> getFirstDegreeConnectionsOfUser(Long userId) {
        log.info("Getting first degree connections of user with ID: {}", userId);

        return personRepository.getFirstDegreeConnections(userId);
    }

    public void sendConnectionRequest(Long receiverId) {
        Long senderId = AuthContextHolder.getCurrentUserId();
        log.info("sending connection request with senderId: {}, receiverId: {}", senderId, receiverId);

        if (senderId.equals(receiverId)) {
            throw new BadRequestException("Both sender and receiver are the same");
        }

        boolean alreadySentRequest = personRepository.connectionRequestExists(senderId, receiverId);
        if (alreadySentRequest) {
            throw new BadRequestException("Connection request already exists, cannot send again");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);
        if (alreadyConnected) {
            throw new BadRequestException("Already connected users, cannot add connection request");
        }

        personRepository.addConnectionRequest(senderId, receiverId);
        log.info("Successfully sent the connection request");

        ConnectionRequestedEvent event = ConnectionRequestedEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        connectionRequestedEventKafkaTemplate.send(
                "connection_requested_topic",
                event
        );


    }

    public void acceptConnectionRequest(Long senderId) {
        Long receiverId = AuthContextHolder.getCurrentUserId();
        log.info("Accepting a connection request with senderId: {}, receiverId: {}", senderId, receiverId);

        if (senderId.equals(receiverId)) {
            throw new BadRequestException("Both sender and receiver are the same");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);
        if (alreadyConnected) {
            throw new BadRequestException("Already connected users, cannot accept connection request again");
        }

        boolean alreadySentRequest = personRepository.connectionRequestExists(senderId, receiverId);
        if (!alreadySentRequest) {
            throw new BadRequestException("No Connection request exists, cannot accept without request");
        }

        personRepository.acceptConnectionRequest(senderId, receiverId);

        log.info("Successfully accepted the connection request with senderId: {}, receiverId: {}", senderId,
                receiverId);

        ConnectionAcceptedEvent event = ConnectionAcceptedEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        connectionAcceptedEventKafkaTemplate.send(
                "connection_accepted_topic",
                event
        );

    }

    public void rejectConnectionRequest(Long senderId) {
        Long receiverId = AuthContextHolder.getCurrentUserId();
        log.info("Rejecting a connection request with senderId: {}, receiverId: {}", senderId, receiverId);

        if (senderId.equals(receiverId)) {
            throw new BadRequestException("Both sender and receiver are the same");
        }

        boolean alreadySentRequest = personRepository.connectionRequestExists(senderId, receiverId);
        if (!alreadySentRequest) {
            throw new BadRequestException("No Connection request exists, cannot reject it");
        }

        personRepository.rejectConnectionRequest(senderId, receiverId);

        log.info("Successfully rejected the connection request with senderId: {}, receiverId: {}", senderId,
                receiverId);
    }

}
