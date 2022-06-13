package com.example.strawberry.adapter.web.v1.controller;

import com.example.strawberry.adapter.web.base.RestApiV1;
import com.example.strawberry.adapter.web.base.VsResponseUtil;
import com.example.strawberry.application.constants.UrlConstant;
import com.example.strawberry.application.service.IFriendShipService;
import com.example.strawberry.application.service.IUserService;
import com.example.strawberry.domain.dto.ResetPasswordDTO;
import com.example.strawberry.domain.dto.UserDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestApiV1
public class UserController {
    private final IUserService userService;
    private final IFriendShipService friendShipService;


    public UserController(IUserService userService, IFriendShipService friendShipService) {
        this.userService = userService;
        this.friendShipService = friendShipService;
    }

    @ApiOperation(value = "Xem danh sách tất cả tài khoản.")
    @GetMapping(UrlConstant.User.DATA_USER)
    public ResponseEntity<?> getAllUsers() {
        return VsResponseUtil.ok(userService.findAllUsers());
    }

    @ApiOperation(value = "Xem tài khoản theo id.")
    @GetMapping(UrlConstant.User.DATA_USER_ID)
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return VsResponseUtil.ok(userService.findUserById(id));
    }

    @ApiOperation(value = "Đăng ký tài khoản.")
    @PostMapping(UrlConstant.User.DATA_USER_REGISTER)
    public ResponseEntity<?> registerUser(
            @RequestBody UserDTO userDTO
    ) {
        return VsResponseUtil.ok(userService.registerUser(userDTO));
    }
    @ApiOperation(value = "Kích hoạt tài khoản.")
    @GetMapping(UrlConstant.User.DATA_USER_REGISTER_ACTIVE)
    public ResponseEntity<?> activeUser(
            @PathVariable("id") Long id,
            @RequestParam(name = "code", required=false) String code) {
        return VsResponseUtil.ok(userService.activeUser(id, code));
    }

    @ApiOperation(value = "Gửi lại code.")
    @GetMapping(UrlConstant.User.DATA_USER_RESEND_CODE)
    public ResponseEntity<?> registerUser(@PathVariable("id") Long id) {
        return VsResponseUtil.ok(userService.resendCode(id));
    }

    @ApiOperation(value = "Quên mật khẩu.")
    @GetMapping(UrlConstant.User.DATA_USER_FORGET_PASSWORD)
    public ResponseEntity<?> forgetPassword(@RequestParam("email") String email) {
        return VsResponseUtil.ok(userService.forgetPassword(email));
    }

    @ApiOperation(value = "Đặt lại mật khẩu.")
    @PostMapping(UrlConstant.User.DATA_USER_CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(
            @PathVariable("id") Long id,
            @RequestBody ResetPasswordDTO resetPasswordDTO) {
        return VsResponseUtil.ok(userService.changePassword(id, resetPasswordDTO));
    }

    @ApiOperation(value = "Cập nhật thông tin tài khoản.")
    @PatchMapping(UrlConstant.User.DATA_USER_UPDATE_USER)
    public ResponseEntity<?> editUserById(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return VsResponseUtil.ok(userService.updateUserById(id, userDTO));
    }

    @ApiOperation(value = "Xoá tài khoản.")
    @DeleteMapping(UrlConstant.User.DATA_USER_DELETE_USER)
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        return VsResponseUtil.ok(userService.deleteUserById(id));
    }

    @ApiOperation(value = "Cập nhật ảnh đại diện.")
    @PostMapping(UrlConstant.User.DATA_USER_UPDATE_AVATAR)
    public ResponseEntity<?> updateAvatarById(
            @PathVariable("id") Long id,
            @RequestParam(name = "avatar", required = false) MultipartFile avatar) throws IOException {
        return VsResponseUtil.ok(userService.updateAvatarById(id, avatar));
    }

    @ApiOperation(value = "Lấy ra tất cả bài viết của user.")
    @GetMapping(UrlConstant.User.DATA_USER_GET_POSTS)
    public ResponseEntity<?> getAllPostById(@PathVariable Long id) {
        return VsResponseUtil.ok(userService.getAllPostByIdUser(id));
    }

    @ApiOperation(value = "Lấy ra tất cả các bài đăng theo quyền truy cập.")
    @GetMapping(UrlConstant.User.DATA_USER_GET_POST_ACCESS)
    public ResponseEntity<?> getAllPostByAccess(
            @PathVariable("idUser") Long id,
            @PathVariable("access") int access
    ) {
        return VsResponseUtil.ok(userService.getAllPostByIdUserAndAccess(id, access));
    }

    @ApiOperation(value = "Lấy ra tất cả nhóm của user.")
    @GetMapping(UrlConstant.User.DATA_USER_GET_GROUPS)
    public ResponseEntity<?> getAllGroupByIdUser(@PathVariable Long id) {
        return VsResponseUtil.ok(userService.getAllGroupByIdUser(id));
    }

    @ApiOperation(value = "Xem tất cả bạn bè")
    @GetMapping(UrlConstant.User.DATA_USER_GET_FRIENDS)
    public ResponseEntity<?> getAllFriend(
            @PathVariable("id") Long id) {
        return VsResponseUtil.ok(friendShipService.getAllFriend(id));
    }

    @ApiOperation(value = "Xem tất cả lời mời kết bạn")
    @GetMapping(UrlConstant.User.DATA_USER_FRIENDS_REQUEST)
    public ResponseEntity<?> createCommentForPost(
            @PathVariable("id") Long id) {
        return VsResponseUtil.ok(friendShipService.getAllRequestAddFriend(id));
    }

    @ApiOperation(value = "Xem tất cả ảnh của user")
    @GetMapping(UrlConstant.User.DATA_IMAGES_OF_USER)
    public ResponseEntity<?> getAllImage(
            @PathVariable("id") Long id) {
        return VsResponseUtil.ok(userService.getAllImageByIdUser(id));
    }

    @ApiOperation(value = "Xem tất cả video của user")
    @GetMapping(UrlConstant.User.DATA_VIDEOS_OF_USER)
    public ResponseEntity<?> getAllVideo(
            @PathVariable("id") Long id) {
        return VsResponseUtil.ok(userService.getAllVideoByIdUser(id));
    }


    // More
    @ApiOperation(value = "Xem danh sách tất cả tài khoản đã đăng ký.")
    @GetMapping("users/register")
    public ResponseEntity<?> getAllUserRegister() {
        return VsResponseUtil.ok(userService.findAllUserRegister());
    }

}
