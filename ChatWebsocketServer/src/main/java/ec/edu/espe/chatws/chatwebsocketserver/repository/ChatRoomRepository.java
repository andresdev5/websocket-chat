package ec.edu.espe.chatws.chatwebsocketserver.repository;

import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoom;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> getAllByTypeOrderByCreatedAtAsc(ChatRoomType type);
    Optional<ChatRoom> findFirstByOwnerIdAndType(long ownerId, ChatRoomType type);
}
