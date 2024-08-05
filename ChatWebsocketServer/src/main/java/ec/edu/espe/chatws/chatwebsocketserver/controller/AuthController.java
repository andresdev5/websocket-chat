package ec.edu.espe.chatws.chatwebsocketserver.controller;

import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.AuthUserPresenter;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.LoginResponsePresenter;
import ec.edu.espe.chatws.chatwebsocketserver.service.AuthenticationService;
import ec.edu.espe.chatws.chatwebsocketserver.jwt.JwtTokenUtil;
import ec.edu.espe.chatws.chatwebsocketserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody AuthUserPresenter credentials) {
        User registeredUser = authenticationService.signup(credentials);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponsePresenter> authenticate(@RequestBody AuthUserPresenter credentials) {
        User authenticatedUser = authenticationService.authenticate(credentials);
        String jwtToken = jwtTokenUtil.generateToken(authenticatedUser);
        LoginResponsePresenter loginResponse = LoginResponsePresenter.builder()
                .token(jwtToken)
                .expiresIn(jwtTokenUtil.getExpiration(jwtToken).getTime())
                .build();

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User> me(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
