package ec.edu.espe.chatws.chatwebsocketserver.repository;

import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatMessage;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomId(long roomId);
    List<ChatMessage> findByChatRoomIdOrderByIdAsc(long roomId);
}
