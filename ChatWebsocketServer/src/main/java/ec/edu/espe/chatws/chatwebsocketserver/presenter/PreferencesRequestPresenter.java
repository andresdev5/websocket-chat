package ec.edu.espe.chatws.chatwebsocketserver.presenter;

import ec.edu.espe.chatws.chatwebsocketserver.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreferencesRequestPresenter {
    private Long userId;
    private String theme;
    private String color;
    private String avatar;
    private String about;
    private UserStatus status;
}
