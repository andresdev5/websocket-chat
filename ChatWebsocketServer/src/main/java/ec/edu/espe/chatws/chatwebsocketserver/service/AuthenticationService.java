package ec.edu.espe.chatws.chatwebsocketserver.service;

import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.AuthUserPresenter;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService {
    User signup(AuthUserPresenter registerUserPresenter);
    User authenticate(AuthUserPresenter loginUserPresenter);
}
