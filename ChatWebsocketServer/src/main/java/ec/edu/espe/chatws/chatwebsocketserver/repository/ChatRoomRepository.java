package ec.edu.espe.chatws.chatwebsocketserver.repository;

import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {}
