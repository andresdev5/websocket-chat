package ec.edu.espe.chatws.chatwebsocketserver.presenter;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ResponseChatMessage {
    private String message;
    private ResponseChatMessageSender sender;
    private Date date;
}
