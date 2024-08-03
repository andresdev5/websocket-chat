package ec.edu.espe.chatws.chatwebsocketserver.service;

import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatMessage;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoom;
import ec.edu.espe.chatws.chatwebsocketserver.entity.User;

import java.util.List;

public interface ChatMessageService {
    ChatMessage addMessage(ChatRoom room, User user, String message);
    List<ChatMessage> findByRoomId(long roomId);
}
