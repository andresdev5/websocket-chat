package ec.edu.espe.chatws.chatwebsocketserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;

    @Builder.Default
    private boolean deleted = false;

    @Builder.Default
    private boolean edited = false;

    @Builder.Default
    private Date createdAt = new Date();

    private Date updatedAt;
    private Date deletedAt;

    @ManyToOne
    private User user;

    @ManyToOne
    private ChatRoom chatRoom;
}
