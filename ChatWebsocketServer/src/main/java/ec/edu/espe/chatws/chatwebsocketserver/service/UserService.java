package ec.edu.espe.chatws.chatwebsocketserver.service;

import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.entity.UserPreference;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.PreferencesRequestPresenter;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user);
    User update(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);
    List<User> findByUsernames(List<String> username);
    UserPreference updateUserPreferences(PreferencesRequestPresenter preferencesRequestPresenter);
}
