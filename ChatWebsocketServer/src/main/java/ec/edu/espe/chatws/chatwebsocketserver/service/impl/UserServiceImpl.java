package ec.edu.espe.chatws.chatwebsocketserver.service.impl;

import ec.edu.espe.chatws.chatwebsocketserver.dto.UserDto;
import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.entity.UserPreference;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.PreferencesRequestPresenter;
import ec.edu.espe.chatws.chatwebsocketserver.repository.UserPreferenceRepository;
import ec.edu.espe.chatws.chatwebsocketserver.repository.UserRepository;
import ec.edu.espe.chatws.chatwebsocketserver.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private ModelMapper modelMapper;

    public UserDto create(User user) {
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    public UserDto update(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User id is required");
        }

        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    public Optional<User> findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return Optional.of(user);
    }

    public Optional<UserDto> findByUsernameAndPassword(String username, String password) {
        return Optional.of(modelMapper.map(userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")), UserDto.class));
    }

    @Override
    public List<UserDto> findByUsernames(List<String> username) {
        return userRepository.findByUsernameIn(username)
                .stream()
                .map(user -> userRepository.findById(user.getId()).get())
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
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
