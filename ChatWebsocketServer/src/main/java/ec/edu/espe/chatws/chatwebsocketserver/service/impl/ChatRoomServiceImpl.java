package ec.edu.espe.chatws.chatwebsocketserver.service.impl;

import ec.edu.espe.chatws.chatwebsocketserver.dto.ChatRoomDto;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoom;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoomType;
import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.repository.ChatRoomRepository;
import ec.edu.espe.chatws.chatwebsocketserver.repository.UserRepository;
import ec.edu.espe.chatws.chatwebsocketserver.service.ChatRoomService;
import ec.edu.espe.chatws.chatwebsocketserver.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ChatRoomDto findById(long roomId) {
        return modelMapper.map(chatRoomRepository.findById(roomId), ChatRoomDto.class);
    }

    @Override
    public List<ChatRoomDto> findAll(ChatRoomType type) {
        return chatRoomRepository.getAllByTypeOrderByCreatedAtAsc(type)
                .stream()
                .map(chatRoom -> modelMapper.map(chatRoom, ChatRoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ChatRoomDto createChatRoom(ChatRoom chatRoom) {
        return modelMapper.map(chatRoomRepository.save(chatRoom), ChatRoomDto.class);
    }

    @Override
    public ChatRoomDto getDirectMessageChatRoom(long ownerId) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        String username = principal.getName();
        User sender = userRepository.findByUsername(username).orElse(null);

        if (sender == null) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "No se pudo recuperar el usuario autentificado");
        }

        User owner = userRepository.findById(ownerId).orElse(null);

        if (owner == null) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Error al obtener el usuario al que se le enviará el mensaje");
        }

        ChatRoom room = chatRoomRepository.findFirstByOwnerIdAndType(ownerId, ChatRoomType.USER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Error al obtener la sala de chat"));

        return modelMapper.map(room, ChatRoomDto.class);
    }

    @Override
    public ChatRoomDto getUserChatRoom(long userId) {
        ChatRoom room = chatRoomRepository.findFirstByOwnerIdAndType(userId, ChatRoomType.USER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Error al obtener la sala de chat"));

        return modelMapper.map(room, ChatRoomDto.class);
    }
}
