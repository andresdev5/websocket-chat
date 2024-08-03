package ec.edu.espe.chatws.chatwebsocketserver.presenter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatEventPresenter {
    private String event;
    private String message;

    @Builder.Default
    private Map<String, Object> data = new HashMap<>();
}
