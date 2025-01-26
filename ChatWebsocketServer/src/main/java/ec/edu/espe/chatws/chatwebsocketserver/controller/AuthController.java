package ec.edu.espe.chatws.chatwebsocketserver.controller;

import ec.edu.espe.chatws.chatwebsocketserver.dto.ChatRoomDto;
import ec.edu.espe.chatws.chatwebsocketserver.dto.UserChatRoomDto;
import ec.edu.espe.chatws.chatwebsocketserver.dto.UserDto;
import ec.edu.espe.chatws.chatwebsocketserver.dto.UserPreferenceDto;
import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.AuthUserPresenter;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.LoginResponsePresenter;
import ec.edu.espe.chatws.chatwebsocketserver.service.AuthenticationService;
import ec.edu.espe.chatws.chatwebsocketserver.jwt.JwtTokenUtil;
import ec.edu.espe.chatws.chatwebsocketserver.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> register(@RequestBody AuthUserPresenter credentials) {
        UserDto registeredUser = authenticationService.signup(credentials);
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
    public ResponseEntity<UserDto> me(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent()) {
            User source = user.get();
            UserDto dto = modelMapper.map(source, UserDto.class);

            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
