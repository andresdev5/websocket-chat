package ec.edu.espe.chatws.chatwebsocketserver.presenter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUserPresenter {
    private String username;
    private String password;
}
