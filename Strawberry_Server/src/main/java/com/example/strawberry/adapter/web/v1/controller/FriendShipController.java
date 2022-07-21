package com.example.strawberry.adapter.web.v1.controller;

import com.example.strawberry.adapter.web.base.RestApiV1;
import com.example.strawberry.adapter.web.base.VsResponseUtil;
import com.example.strawberry.application.constants.UrlConstant;
import com.example.strawberry.application.service.IFriendShipService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestApiV1
public class FriendShipController {
    private final IFriendShipService friendShipService;

    public FriendShipController(IFriendShipService friendShipService) {
        this.friendShipService = friendShipService;
    }

    @ApiOperation(value = "Gửi lời mời kết bạn")
    @PostMapping(UrlConstant.FriendShip.DATA_FRIEND_ADD_FRIEND)
    public ResponseEntity<?> createCommentForPost(
            @PathVariable("idUserSender") Long idUserSender,
            @PathVariable("idUserReceiver") Long idUserReceiver) {
        return VsResponseUtil.ok(friendShipService.addFriend(idUserSender, idUserReceiver));
    }

    @ApiOperation(value = "Huỷ gửi lời mời kết bạn")
    @DeleteMapping(UrlConstant.FriendShip.DATA_FRIEND_CANCEL_ADD_FRIEND)
    public ResponseEntity<?> cancelAddFriend(
            @PathVariable("idUserSender") Long idUserSender,
            @PathVariable("idUserReceiver") Long idUserReceiver) {
        return VsResponseUtil.ok(friendShipService.cancelAddFriend(idUserSender, idUserReceiver));
    }

    @ApiOperation(value = "Chấp nhận lời mời kết bạn")
    @PostMapping(UrlConstant.FriendShip.DATA_FRIEND_ACCEPT_FRIEND)
    public ResponseEntity<?> acceptFriend(
            @PathVariable("idUserSender") Long idUserSender,
            @PathVariable("idUserReceiver") Long idUserReceiver) {
        return VsResponseUtil.ok(friendShipService.acceptFriend(idUserSender, idUserReceiver));
    }

    @ApiOperation(value = "Huỷ kết bạn")
    @DeleteMapping(UrlConstant.FriendShip.DATA_FRIEND_UN_FRIEND)
    public ResponseEntity<?> unFriend(
            @PathVariable("idUserSender") Long idUserSender,
            @PathVariable("idUserReceiver") Long idUserReceiver) {
        return VsResponseUtil.ok(friendShipService.unFriend(idUserSender, idUserReceiver));
    }
}
