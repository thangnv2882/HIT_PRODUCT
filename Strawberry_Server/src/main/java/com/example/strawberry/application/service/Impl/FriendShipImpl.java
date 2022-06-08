package com.example.strawberry.application.service.Impl;

import com.example.strawberry.application.dai.IFriendShipRepository;
import com.example.strawberry.application.dai.IUserRepository;
import com.example.strawberry.application.service.IFriendShipService;
import com.example.strawberry.domain.entity.FriendShip;
import com.example.strawberry.domain.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class FriendShipImpl implements IFriendShipService {

    private final IFriendShipRepository friendShipRepository;
    private final IUserRepository userRepository;
    private final UserServiceImpl userService;


    public FriendShipImpl(IFriendShipRepository friendShipRepository, IUserRepository userRepository, UserServiceImpl userService) {
        this.friendShipRepository = friendShipRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @Override
    public Set<User> getAllFriend(Long id) {
        Optional<User> user = userRepository.findById(id);
        userService.checkUserExists(user);

        Set<User> listUser = new HashSet<>();

        Set<FriendShip> friendShip1 = friendShipRepository.findAllByUserSenderIdAndIsAccept(id, Boolean.TRUE);
        friendShip1.forEach(i -> {
            listUser.add(i.getUserReceiver());
        });

        Set<FriendShip> friendShip2 = friendShipRepository.findAllByUserReceiverIdAndIsAccept(id, Boolean.TRUE);
        friendShip2.forEach(i -> {
            listUser.add(i.getUserSender());
        });
        return listUser;
    }

    @Override
    public Set<User> getAllRequestAddFriend(Long idUserReceiver) {
        Optional<User> user = userRepository.findById(idUserReceiver);
        userService.checkUserExists(user);

        Set<FriendShip> friendShip = friendShipRepository.findAllByUserReceiverIdAndIsAccept(idUserReceiver, Boolean.FALSE);
        Set<User> users = new HashSet<>();
        friendShip.forEach(i -> {
            users.add(i.getUserSender());
        });
        return users;
    }

    @Override
    public String addFriend(Long idUserSender, Long idUserReceiver) {
        FriendShip friendShip = friendShipRepository.findFriendShipByUserSenderIdAndUserReceiverId(idUserSender, idUserReceiver);

        if (friendShip != null) {
            if (friendShip.getIsAccept() == Boolean.FALSE) {
                return "You have already sent a friend request";
            } else {
                return "Already friends";
            }
        }

        friendShip = new FriendShip();

        Optional<User> userSender = userRepository.findById(idUserSender);
        userService.checkUserExists(userSender);
        Optional<User> userReceiver = userRepository.findById(idUserReceiver);
        userService.checkUserExists(userReceiver);

        friendShip.setUserSender(userSender.get());
        friendShip.setUserReceiver(userReceiver.get());
        friendShip.setIsAccept(Boolean.FALSE);

        friendShipRepository.save(friendShip);
        return "Sent a friend request";
    }

    @Override
    public String cancelAddFriend(Long idUserSender, Long idUserReceiver) {
        FriendShip friendShip = friendShipRepository.findFriendShipByUserSenderIdAndUserReceiverId(idUserSender, idUserReceiver);
        if (friendShip != null) {
            friendShipRepository.delete(friendShip);
            return "Canceled friend request.";
        }
        return "You don't have a friend request with this person yet.";
    }

    @Override
    public String acceptFriend(Long idUserSender, Long idUserReceiver) {
        FriendShip friendShip = friendShipRepository.findFriendShipByUserSenderIdAndUserReceiverId(idUserSender, idUserReceiver);
        if (friendShip != null) {
            if (friendShip.getIsAccept() == Boolean.FALSE) {
                friendShip.setIsAccept(Boolean.TRUE);
                friendShipRepository.save(friendShip);
                return "Accept friend request.";
            }
            return "Already friends";
        }
        return "Don't accept friend requests.";
    }

    @Override
    public String unFriend(Long idUserSender, Long idUserReceiver) {
        FriendShip friendShip = friendShipRepository
                .findFriendShipByUserSenderIdAndUserReceiverIdAndIsAccept(idUserSender, idUserReceiver, Boolean.TRUE);
        if (friendShip != null) {
            friendShipRepository.delete(friendShip);
            return "Unfriended successfully.";
        }
        friendShip = friendShipRepository.findFriendShipByUserSenderIdAndUserReceiverIdAndIsAccept(idUserReceiver, idUserSender, Boolean.TRUE);
        if (friendShip != null) {
            friendShipRepository.delete(friendShip);
            return "Unfriended successfully.";
        }
        return "Unfriend failed.";
    }


}
