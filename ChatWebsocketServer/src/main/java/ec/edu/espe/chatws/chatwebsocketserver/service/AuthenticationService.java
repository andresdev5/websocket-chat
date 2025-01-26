package ec.edu.espe.chatws.chatwebsocketserver.service;

import ec.edu.espe.chatws.chatwebsocketserver.dto.UserDto;
import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.AuthUserPresenter;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService {
    UserDto signup(AuthUserPresenter registerUserPresenter);
    User authenticate(AuthUserPresenter loginUserPresenter);
}
