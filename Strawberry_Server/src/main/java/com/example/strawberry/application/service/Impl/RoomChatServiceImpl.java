package com.example.strawberry.application.service.Impl;

import com.example.strawberry.application.constants.MessageConstant;
import com.example.strawberry.application.dai.IRoomChatRepository;
import com.example.strawberry.application.dai.IUserRepository;
import com.example.strawberry.application.service.IRoomChatService;
import com.example.strawberry.config.exception.ExceptionAll;
import com.example.strawberry.config.exception.NotFoundException;
import com.example.strawberry.domain.dto.RoomChatDTO;
import com.example.strawberry.domain.entity.Message;
import com.example.strawberry.domain.entity.RoomChat;
import com.example.strawberry.domain.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RoomChatServiceImpl implements IRoomChatService {

    private final IRoomChatRepository roomChatRepository;
    private final ModelMapper modelMapper;
    private final UserServiceImpl userService;
    private final IUserRepository userRepository;

    public RoomChatServiceImpl(IRoomChatRepository roomChatRepository, ModelMapper modelMapper, UserServiceImpl userService, IUserRepository userRepository) {
        this.roomChatRepository = roomChatRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public RoomChat createRoomChat(RoomChatDTO roomChatDTO) {
        Optional<User> user = userRepository.findById(roomChatDTO.getIdUserBoss());
        userService.checkUserExists(user);

        RoomChat roomChat = modelMapper.map(roomChatDTO, RoomChat.class);
        Set<User> users = new HashSet<>();
        users.add(user.get());
        roomChat.setUsers(users);

        roomChatRepository.save(roomChat);
        return roomChat;
    }

    @Override
    public RoomChat deleteRoomChat(Long idRoomChat, Long idUserBoss) {
        Optional<RoomChat> roomChat = roomChatRepository.findById(idRoomChat);
        checkRoomChatExists(roomChat);
        Set<User> users = roomChat.get().getUsers();
        if(users.stream().findFirst().get().getId() != idUserBoss) {
            throw new ExceptionAll("Can't delete chat room because you're not the manager");
        }
        roomChatRepository.delete(roomChat.get());
        return roomChat.get();
    }

    @Override
    public Set<Message> getAllMessageInRoomChat(Long idRoomChat) {
        Optional<RoomChat> roomChat = roomChatRepository.findById(idRoomChat);
        checkRoomChatExists(roomChat);
        Set<Message> messages = roomChat.get().getMessages();
        return messages;
    }

    @Override
    public Set<User> getAllUserInRoomChat(Long idRoomChat) {
        Optional<RoomChat> roomChat = roomChatRepository.findById(idRoomChat);
        checkRoomChatExists(roomChat);
        Set<User> users = roomChat.get().getUsers();
        return users;
    }

    public void checkRoomChatExists(Optional<RoomChat> roomChat) {
        if (roomChat.isEmpty()) {
            throw new NotFoundException(MessageConstant.ROOMCHAT_NOT_EXISTS);
        }
    }
}




