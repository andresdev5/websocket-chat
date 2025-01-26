package ec.edu.espe.chatws.chatwebsocketserver.dto;

import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoomType;
import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDto {
    private Long id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private ChatRoomType type;
    private UserDto owner;
    private Date createdAt;
}
