package ec.edu.espe.chatws.chatwebsocketserver.controller;

import ec.edu.espe.chatws.chatwebsocketserver.dto.UserDto;
import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/information")
public class InformationController {
    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/connections")
    public List<UserDto> getConnections() {
        List<String> connections = new ArrayList<>();

        simpUserRegistry.getUsers().forEach(user -> {
            connections.add(user.getName());
        });

        return userService.findByUsernames(connections)
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
}
