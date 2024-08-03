package ec.edu.espe.chatws.chatwebsocketserver.controller;

import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatMessage;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoom;
import ec.edu.espe.chatws.chatwebsocketserver.service.ChatMessageService;
import ec.edu.espe.chatws.chatwebsocketserver.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat-rooms")
public class ChatRoomController {
    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping
    public List<ChatRoom> getChatRooms() {
        return chatRoomService.findAll();
    }

    @GetMapping("/{roomId}/messages")
    public List<ChatMessage> getChatRoomMessages(@PathVariable long roomId) {
        return chatMessageService.findByRoomId(roomId);
    }
}
