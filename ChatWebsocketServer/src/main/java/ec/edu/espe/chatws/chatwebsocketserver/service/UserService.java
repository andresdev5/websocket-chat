package ec.edu.espe.chatws.chatwebsocketserver.service;

import ec.edu.espe.chatws.chatwebsocketserver.dto.UserDto;
import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.entity.UserPreference;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.PreferencesRequestPresenter;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDto create(User user);
    UserDto update(User user);
    Optional<User> findByUsername(String username);
    Optional<UserDto> findByUsernameAndPassword(String username, String password);
    List<UserDto> findByUsernames(List<String> username);
    UserPreference updateUserPreferences(PreferencesRequestPresenter preferencesRequestPresenter);
}
