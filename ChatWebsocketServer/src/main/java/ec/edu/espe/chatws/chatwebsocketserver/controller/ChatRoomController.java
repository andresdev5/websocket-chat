package ec.edu.espe.chatws.chatwebsocketserver.controller;

import ec.edu.espe.chatws.chatwebsocketserver.dto.ChatMessageDto;
import ec.edu.espe.chatws.chatwebsocketserver.dto.ChatRoomDto;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatMessage;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoom;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoomType;
import ec.edu.espe.chatws.chatwebsocketserver.service.ChatMessageService;
import ec.edu.espe.chatws.chatwebsocketserver.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat-rooms")
public class ChatRoomController {
    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping
    public List<ChatRoomDto> getChatRooms(@RequestParam(defaultValue = "CHANNEL") ChatRoomType type) {
        return chatRoomService.findAll(type);
    }

    @PostMapping("/create")
    public ChatRoomDto createChatRoom(@RequestBody ChatRoom chatRoom) {
        return chatRoomService.createChatRoom(chatRoom);
    }

    @GetMapping("/dm/{ownerId}")
    public ChatRoomDto getDirectMessageChatRoom(@PathVariable long ownerId) {
        return chatRoomService.getDirectMessageChatRoom(ownerId);
    }

    @GetMapping("/{roomId}/messages")
    public List<ChatMessageDto> getChatRoomMessages(@PathVariable long roomId) {
        return chatMessageService.findByRoomId(roomId);
    }
}
