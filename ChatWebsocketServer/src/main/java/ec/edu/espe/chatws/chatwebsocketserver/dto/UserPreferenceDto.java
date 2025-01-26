package ec.edu.espe.chatws.chatwebsocketserver.dto;

import ec.edu.espe.chatws.chatwebsocketserver.entity.UserStatus;
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
public class UserPreferenceDto {
    private Long id;
    private String theme;
    private String color;
    private String avatar;
    private String about;
    private UserStatus status;
}
