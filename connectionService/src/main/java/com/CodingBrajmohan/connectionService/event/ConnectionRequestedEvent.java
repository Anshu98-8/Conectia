package com.CodingBrajmohan.connectionService.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionRequestedEvent {
    private Long senderId;
    private Long receiverId;
}
