package ec.edu.espe.chatws.chatwebsocketserver.service.impl;

import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatMessage;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoom;
import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.repository.ChatMessageRepository;
import ec.edu.espe.chatws.chatwebsocketserver.service.ChatMessageService;
import org.apache.commons.text.StringEscapeUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage addMessage(ChatRoom room, User user, String message) {
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(room)
                .user(user)
                .message(message)
                .createdAt(new Date())
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> findByRoomId(long roomId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByIdAsc(roomId);
        Parser parser = Parser.builder().build();

        for (ChatMessage message : messages) {
            String content = (message.getMessage());
            Node document = parser.parse(content);
            HtmlRenderer renderer = HtmlRenderer.builder()
                    .escapeHtml(true).build();
            message.setMessage(renderer.render(document));
        }

        return messages;
    }
}
