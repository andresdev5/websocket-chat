package ec.edu.espe.chatws.chatwebsocketserver.service.impl;

import ec.edu.espe.chatws.chatwebsocketserver.dto.ChatMessageDto;
import ec.edu.espe.chatws.chatwebsocketserver.dto.ChatRoomDto;
import ec.edu.espe.chatws.chatwebsocketserver.dto.UserDto;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatMessage;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoom;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoomType;
import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.repository.ChatMessageRepository;
import ec.edu.espe.chatws.chatwebsocketserver.repository.ChatRoomRepository;
import ec.edu.espe.chatws.chatwebsocketserver.repository.UserRepository;
import ec.edu.espe.chatws.chatwebsocketserver.service.ChatMessageService;
import org.apache.commons.text.StringEscapeUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ChatMessageDto addMessage(ChatRoomDto roomDto, Long userId, Long receiverId, String message) {
        ChatRoom room = chatRoomRepository.findById(roomDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Sala de chat no encontrada"));

        User receiver = null;

        if (receiverId != null) {
            receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Usuario no encontrado"));
        }

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Usuario no encontrado"));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(room)
                .user(targetUser)
                .receiver(receiver)
                .message(message)
                .createdAt(new Date())
                .build();

        ChatMessage saved = chatMessageRepository.save(chatMessage);
        return modelMapper.map(saved, ChatMessageDto.class);
    }

    @Override
    public List<ChatMessageDto> findByRoomId(long roomId) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Sala de chat no encontrada"));

        List<ChatMessage> messages;

        if (room.getType() == ChatRoomType.CHANNEL) {
            messages = chatMessageRepository.findChatRoomMessages(room.getId());
        } else {
            messages = chatMessageRepository.findChatRoomDirectMessages(user.getId());
        }

        Parser parser = Parser.builder().build();

        for (ChatMessage message : messages) {
            String content = (message.getMessage());
            Node document = parser.parse(content);
            HtmlRenderer renderer = HtmlRenderer.builder()
                    .escapeHtml(true).build();
            message.setMessage(renderer.render(document));
        }

        return messages.stream()
                .map(chatMessage -> modelMapper.map(chatMessage, ChatMessageDto.class))
                .toList();
    }
}
