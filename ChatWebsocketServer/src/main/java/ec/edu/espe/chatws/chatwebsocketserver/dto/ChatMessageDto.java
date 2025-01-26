package ec.edu.espe.chatws.chatwebsocketserver.dto;

import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoom;
import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    private Long id;
    private String message;
    private boolean deleted;
    private boolean edited;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private UserDto user;
    private UserDto receiver;
    private ChatRoomDto chatRoom;
}
