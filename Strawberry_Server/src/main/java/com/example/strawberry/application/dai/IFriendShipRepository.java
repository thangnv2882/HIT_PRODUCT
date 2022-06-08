package com.example.strawberry.application.dai;

import com.example.strawberry.domain.entity.FriendShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IFriendShipRepository extends JpaRepository<FriendShip, Long> {

    @Query("select f from FriendShip f where f.userSender.id = ?1 or f.userReceiver.id = ?2")
    Set<FriendShip> findAllByUserSenderIdOrUserReceiverId(Long idUserSender, Long idUserReceiver);

    @Query("select f from FriendShip f where f.userReceiver.id = ?1 and f.isAccept = ?2")
    Set<FriendShip> findAllByUserReceiverIdAndIsAccept(Long idUserReceiver, Boolean boolen);

    @Query("select f from FriendShip f where f.userSender.id = ?1 and f.isAccept = ?2")
    Set<FriendShip> findAllByUserSenderIdAndIsAccept(Long idUserSender, Boolean boolen);

    @Query("select f from FriendShip f where f.userSender.id = ?1 and f.userReceiver.id = ?2")
    FriendShip findFriendShipByUserSenderIdAndUserReceiverId(Long idUserSender, Long idUserReceiver);

    @Query("select f from FriendShip f where f.userReceiver.id = ?1 and f.userSender.id = ?2")
    FriendShip findFriendShipByUserReceiverIdAndUserSenderId(Long idUserReceiver, Long idUserSender);


    @Query("select f from FriendShip f where f.userSender.id = ?1 and f.userReceiver.id = ?2 and f.isAccept = ?3")
    FriendShip findFriendShipByUserSenderIdAndUserReceiverIdAndIsAccept(Long idUserSender, Long idUserReceiver, Boolean boolen);


//    @Query("select f from FriendShip f " +
//            "where (f.userSender.id = 1 and f.userReceiver.id = 2) or (f.userSender.id = 2 and f.userReceiver.id = 1)")
//    FriendShip findFriendShipSendOrReceive(Long idUserSender1, Long idUserReceiver1);

}
