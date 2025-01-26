package ec.edu.espe.chatws.chatwebsocketserver.repository;

import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatMessage;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomId(long roomId);
    List<ChatMessage> findByChatRoomIdOrderByIdAsc(long roomId);

    @Query("SELECT m " +
            "FROM ChatMessage m " +
            "WHERE m.chatRoom.id = :roomId " +
            "AND m.chatRoom.type = 'CHANNEL' " +
            "ORDER BY m.createdAt ASC")
    List<ChatMessage> findChatRoomMessages(long roomId);

    @Query("SELECT m " +
            "FROM ChatMessage m " +
            "WHERE m.chatRoom.type = 'USER' " +
            "AND (m.receiver.id = ?1 OR m.user.id = ?1) " +
            "ORDER BY m.createdAt ASC")
    List<ChatMessage> findChatRoomDirectMessages(long userId);
}
