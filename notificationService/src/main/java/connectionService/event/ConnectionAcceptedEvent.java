package connectionService.event;

import lombok.Data;

@Data
public class ConnectionAcceptedEvent {
    private Long receiverId;
    private Long senderId;
}
