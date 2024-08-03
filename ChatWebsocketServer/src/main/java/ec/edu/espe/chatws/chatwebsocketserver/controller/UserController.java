package ec.edu.espe.chatws.chatwebsocketserver.controller;

import ec.edu.espe.chatws.chatwebsocketserver.entity.UserPreference;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.ChatEventPresenter;
import ec.edu.espe.chatws.chatwebsocketserver.presenter.PreferencesRequestPresenter;
import ec.edu.espe.chatws.chatwebsocketserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserService userService;

    @PutMapping("/preferences")
    public UserPreference updatePreferences(@RequestBody PreferencesRequestPresenter preferencesRequestPresenter) {
        UserPreference preferences = userService.updateUserPreferences(preferencesRequestPresenter);
        simpMessagingTemplate.convertAndSend("/topic/event", ChatEventPresenter.builder()
                .event("UPDATE_USER_PREFERENCES")
                .data(Map.of(
                    "userId", preferencesRequestPresenter.getUserId(),
                    "oldPreferences", preferencesRequestPresenter,
                    "updatedPreferences", preferences
                ))
                .build());

        return preferences;
    }
}
