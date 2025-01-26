package ec.edu.espe.chatws.chatwebsocketserver.service;

import ec.edu.espe.chatws.chatwebsocketserver.dto.ChatRoomDto;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoom;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoomType;

import java.util.List;

public interface ChatRoomService {
    ChatRoomDto findById(long roomId);
    List<ChatRoomDto> findAll(ChatRoomType type);
    ChatRoomDto createChatRoom(ChatRoom chatRoom);
    ChatRoomDto getDirectMessageChatRoom(long ownerId);
    ChatRoomDto getUserChatRoom(long userId);
}
