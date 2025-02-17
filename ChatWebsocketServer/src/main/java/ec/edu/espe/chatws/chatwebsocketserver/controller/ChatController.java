package ec.edu.espe.chatws.chatwebsocketserver.controller;

import ec.edu.espe.chatws.chatwebsocketserver.dto.ChatMessageDto;
import ec.edu.espe.chatws.chatwebsocketserver.dto.ChatRoomDto;
import ec.edu.espe.chatws.chatwebsocketserver.dto.UserDto;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatMessage;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoom;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoomType;
import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.ChatEventPresenter;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.ResponseChatMessage;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.ResponseChatMessageSender;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.SenderChatType;
import ec.edu.espe.chatws.chatwebsocketserver.service.ChatMessageService;
import ec.edu.espe.chatws.chatwebsocketserver.service.ChatRoomService;
import ec.edu.espe.chatws.chatwebsocketserver.service.UserService;
import org.apache.commons.text.StringEscapeUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private UserService userService;

    @MessageMapping("/message/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, ChatMessage message, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username).orElse(null);
        ChatRoomDto room = chatRoomService.findById(Long.parseLong(roomId));

        if (user == null || room == null) {
            throw new IllegalArgumentException("Invalid user or room");
        }

        Long receiverId = message.getReceiver() != null ? message.getReceiver().getId() : null;
        ChatMessageDto chatMessage = chatMessageService.addMessage(room, user.getId(), receiverId, message.getMessage());
        String content = (message.getMessage());

        Parser parser = Parser.builder().build();
        Node document = parser.parse(content);
        HtmlRenderer renderer = HtmlRenderer.builder().escapeHtml(true).build();
        chatMessage.setMessage(renderer.render(document));

        boolean isDm = room.getType() == ChatRoomType.USER && (
                chatMessage.getUser().getId().equals(user.getId())
                        || chatMessage.getReceiver().getId().equals(user.getId()));

        boolean canNotify = room.getType() == ChatRoomType.CHANNEL || isDm;

        Map<String, Object> params = new HashMap<>(Map.of(
                "messageId", chatMessage.getId(),
                "roomId", roomId,
                "senderId", user.getId(),
                "isDm", isDm
        ));

        simpMessagingTemplate.convertAndSend("/queue/reply/" + roomId, chatMessage);
        simpMessagingTemplate.convertAndSend("/topic/event", ChatEventPresenter.builder()
                .event("MESSAGE")
                .data(params)
                .build());

        if (isDm) {
            ChatRoomDto sourceRoom = chatRoomService.getUserChatRoom(user.getId());
            params.put("receiverId", chatMessage.getReceiver().getId());
            params.put("roomId", sourceRoom.getId());

            simpMessagingTemplate.convertAndSend("/queue/reply/" + sourceRoom.getId(), chatMessage);
            simpMessagingTemplate.convertAndSend("/topic/event", ChatEventPresenter.builder()
                    .event("MESSAGE")
                    .data(params)
                    .build());
        }
    }

    @MessageMapping("/notify-event")
    public void notifyEvent(ChatEventPresenter event) {
        simpMessagingTemplate.convertAndSend("/topic/event", event);
    }

    @MessageExceptionHandler
    @SendTo("/queue/errors")
    public ResponseChatMessage handleException(Throwable exception) {
        return ResponseChatMessage.builder()
                .message(exception.getMessage())
                .sender(ResponseChatMessageSender.builder()
                        .username("system")
                        .color("#ffffff")
                        .type(SenderChatType.SYSTEM)
                        .build())
                .date(new Date())
                .build();
    }
}
