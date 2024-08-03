package ec.edu.espe.chatws.chatwebsocketserver.service.impl;

import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.entity.UserPreference;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.PreferencesRequestPresenter;
import ec.edu.espe.chatws.chatwebsocketserver.repository.UserPreferenceRepository;
import ec.edu.espe.chatws.chatwebsocketserver.repository.UserRepository;
import ec.edu.espe.chatws.chatwebsocketserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User id is required");
        }

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public List<User> findByUsernames(List<String> username) {
        return userRepository.findByUsernameIn(username);
    }

    @Override
    public UserPreference updateUserPreferences(PreferencesRequestPresenter preferencesRequestPresenter) {
        User user = userRepository.findById(preferencesRequestPresenter.getUserId())
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found"));
        UserPreference userPreference = userPreferenceRepository.findById(user.getPreferences().getId())
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found"));

        if (preferencesRequestPresenter.getAbout() != null) {
            userPreference.setAbout(preferencesRequestPresenter.getAbout());
        }

        if (preferencesRequestPresenter.getAvatar() != null) {
            userPreference.setAvatar(preferencesRequestPresenter.getAvatar());
        }

        if (preferencesRequestPresenter.getTheme() != null) {
            userPreference.setTheme(preferencesRequestPresenter.getTheme());
        }

        if (preferencesRequestPresenter.getStatus() != null) {
            userPreference.setStatus(preferencesRequestPresenter.getStatus());
        }

        return userPreferenceRepository.save(userPreference);
    }
}
