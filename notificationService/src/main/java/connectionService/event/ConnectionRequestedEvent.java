package connectionService.event;

import lombok.Data;

@Data
public class ConnectionRequestedEvent {
    private Long senderId;
    private Long receiverId;
}
