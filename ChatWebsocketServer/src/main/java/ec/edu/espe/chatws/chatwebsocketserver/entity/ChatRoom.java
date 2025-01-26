package ec.edu.espe.chatws.chatwebsocketserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ChatRoomType type = ChatRoomType.CHANNEL;

    @OneToOne
    @JoinColumn(name = "owner_id")
    @ToString.Exclude
    private User owner;

    @Builder.Default
    private Date createdAt = new Date();
}
