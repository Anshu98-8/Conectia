package com.CodingBrajmohan.connectionService.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionAcceptedEvent {
    private Long receiverId;
    private Long senderId;
}
