package ec.edu.espe.chatws.chatwebsocketserver.service.impl;

import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.entity.UserPreference;
import ec.edu.espe.chatws.chatwebsocketserver.entity.UserRole;
import ec.edu.espe.chatws.chatwebsocketserver.entity.UserStatus;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.AuthUserPresenter;
import ec.edu.espe.chatws.chatwebsocketserver.repository.UserPreferenceRepository;
import ec.edu.espe.chatws.chatwebsocketserver.repository.UserRepository;
import ec.edu.espe.chatws.chatwebsocketserver.service.AuthenticationService;
import ec.edu.espe.chatws.chatwebsocketserver.utils.ColorUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public User signup(AuthUserPresenter input) {
        Pair<String, String> color = ColorUtils.getInstance().getRandomFlatColor();
        String avatar = "https://ui-avatars.com/api/?name=" +
                input.getUsername() +
                "&size=128&bold=true" +
                String.format("&background=%s&color=%s",
                        color.getLeft().replace("#", ""),
                        color.getRight().replace("#", ""));
        UserPreference preferences = UserPreference.builder()
                .theme("dark")
                .color(color.getLeft())
                .status(UserStatus.ONLINE)
                .avatar(avatar)
                .build();

        Optional<User> existingUser = userRepository.findByUsername(input.getUsername());

        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "User already exists");
        }

        UserPreference savedPreferences = userPreferenceRepository.save(preferences);

        User user = User.builder()
                .username(input.getUsername())
                .password(passwordEncoder.encode(input.getPassword()))
                .role(UserRole.USER)
                .preferences(savedPreferences)
                .build();

        return userRepository.save(user);
    }

    public User authenticate(AuthUserPresenter input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
