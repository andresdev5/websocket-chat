package ec.edu.espe.chatws.chatwebsocketserver.config;

import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.entity.UserRole;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.AuthUserPresenter;
import ec.edu.espe.chatws.chatwebsocketserver.repository.UserRepository;
import ec.edu.espe.chatws.chatwebsocketserver.service.AuthenticationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppInitializer {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @PostConstruct
    public void init() {
        createAdminUser();
    }

    private void createAdminUser() {
        if (userRepository.findByUsername("admin").isPresent()) {
            return;
        }

        authenticationService.signup(AuthUserPresenter.builder()
                .username("admin")
                .password("admin")
                .build());

        User admin = userRepository.findByUsername("admin").get();
        admin.setRole(UserRole.ADMIN);
        userRepository.save(admin);
    }
}
