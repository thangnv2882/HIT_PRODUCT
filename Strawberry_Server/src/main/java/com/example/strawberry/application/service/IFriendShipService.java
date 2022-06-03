package com.example.strawberry.application.service;

import com.example.strawberry.domain.entity.FriendShip;
import com.example.strawberry.domain.entity.User;

import java.util.Set;

public interface IFriendShipService {
    Set<User> getAllFriend(Long id);
    Set<User> getAllRequestAddFriend(Long idUserReceiver);

    String addFriend(Long idUserSender, Long idUserReceiver);
    String cancelAddFriend(Long idUserSender, Long idUserReceiver);
    String acceptFriend(Long idUserReceiver, Long idUserSender);
    String unFriend(Long idUserSender, Long idUserReceiver);


}
