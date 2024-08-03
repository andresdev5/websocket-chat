package ec.edu.espe.chatws.chatwebsocketserver.presenter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseChatMessageSender {
    private String username;
    private SenderChatType type;
    private String color;
}
