package ec.edu.espe.chatws.chatwebsocketserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoom;
import ec.edu.espe.chatws.chatwebsocketserver.entity.UserPreference;
import ec.edu.espe.chatws.chatwebsocketserver.entity.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    private ChatRoomDto chatRoom;

    private UserPreferenceDto preferences;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
}
