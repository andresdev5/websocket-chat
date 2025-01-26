package ec.edu.espe.chatws.chatwebsocketserver.service;

import ec.edu.espe.chatws.chatwebsocketserver.dto.ChatMessageDto;
import ec.edu.espe.chatws.chatwebsocketserver.dto.ChatRoomDto;
import ec.edu.espe.chatws.chatwebsocketserver.dto.UserDto;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatMessage;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoom;
import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageService {
    ChatMessageDto addMessage(ChatRoomDto roomDto, Long userId, Long receiverId, String message);
    List<ChatMessageDto> findByRoomId(long roomId);
}
