package ec.edu.espe.chatws.chatwebsocketserver.config;

import ec.edu.espe.chatws.chatwebsocketserver.presenter.ChatEventPresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChatHandlerEvent {
    private final SimpUserRegistry userRegistry;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        String username = event.getUser().getName();
        messagingTemplate.convertAndSend("/topic/event", ChatEventPresenter.builder()
                .event("CONNECTED")
                .message(username + " se ha conectado.")
                .data(Map.of("username", username))
                .build());
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String username = event.getUser().getName();
        messagingTemplate.convertAndSend("/topic/event", ChatEventPresenter.builder()
                .event("DISCONNECTED")
                .message(username + " se ha conectado.")
                .data(Map.of("username", username))
                .build());
    }
}
