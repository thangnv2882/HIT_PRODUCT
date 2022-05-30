package com.example.strawberry.adapter.web.v1.controller;

import com.example.strawberry.adapter.web.base.VsResponseUtil;
import com.example.strawberry.application.service.IUserService;
//import com.example.strawberry.domain.dto.ResetPasswordDTO;
import com.example.strawberry.domain.dto.ResetPasswordDTO;
import com.example.strawberry.domain.dto.UserDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Xem tài khoản theo id.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return VsResponseUtil.ok(userService.findUserById(id));
    }

    @ApiOperation(value = "Xem danh sách tất cả tài khoản.")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return VsResponseUtil.ok(userService.findAllUsers());
    }


    @ApiOperation(value = "Đăng ký tài khoản.")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserDTO userDTO
    ) {
        return VsResponseUtil.ok(userService.registerUser(userDTO));
    }
    @ApiOperation(value = "Kích hoạt tài khoản.")
    @GetMapping("/register/{id}/active")
    public ResponseEntity<?> activeUser(
            @PathVariable("id") Long id,
            @RequestParam(name = "code", required=false) String code) {
        return VsResponseUtil.ok(userService.activeUser(id, code));
    }

    @ApiOperation(value = "Gửi lại code.")
    @GetMapping("/register/{id}/resend-code")
    public ResponseEntity<?> registerUser(@PathVariable("id") Long id) {
        return VsResponseUtil.ok(userService.resendCode(id));
    }

    @ApiOperation(value = "Quên mật khẩu.")
    @GetMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestParam("email") String email) {
        return VsResponseUtil.ok(userService.forgetPassword(email));
    }

    @ApiOperation(value = "Đặt lại mật khẩu.")
    @PostMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(
            @PathVariable("id") Long id,
            @RequestBody ResetPasswordDTO resetPasswordDTO) {
        return VsResponseUtil.ok(userService.changePassword(id, resetPasswordDTO));
    }

    @ApiOperation(value = "Cập nhật thông tin tài khoản.")
    @PatchMapping("/{id}/update-user")
    public ResponseEntity<?> editUserById(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return VsResponseUtil.ok(userService.updateUserById(id, userDTO));
    }

    @ApiOperation(value = "Xoá tài khoản.")
    @DeleteMapping("/{id}/delete-user")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        return VsResponseUtil.ok(userService.deleteUserById(id));
    }

    @ApiOperation(value = "Cập nhật ảnh đại diện.")
    @PostMapping("/{id}/avatar")
    public ResponseEntity<?> updateAvatarById(
            @PathVariable("id") Long id,
            @RequestParam(name = "avatar", required = false) MultipartFile avatar) throws IOException {
        return VsResponseUtil.ok(userService.updateAvatarById(id, avatar));
    }

    @ApiOperation(value = "Lấy ra tất cả bài viết của user theo id.")
    @GetMapping("/{id}/posts")
    public ResponseEntity<?> getAllPostById(@PathVariable Long id) {
        return VsResponseUtil.ok(userService.getAllPostById(id));
    }

    @ApiOperation(value = "Lấy ra tất cả các bài đăng theo quyền truy cập.")
    @GetMapping("/{idUser}/posts/{access}")
    public ResponseEntity<?> getAllPostByAccess(
            @PathVariable("idUser") Long id,
            @PathVariable("access") int access
    ) {
        return VsResponseUtil.ok(userService.getAllPostByAccess(id, access));
    }

    @ApiOperation(value = "Lấy ra tất cả nhóm của user theo id.")
    @GetMapping("/{id}/groups")
    public ResponseEntity<?> getAllGroupByIdUser(@PathVariable Long id) {
        return VsResponseUtil.ok(userService.getAllGroupByIdUser(id));
    }
//
//    @ApiOperation(value = "Gửi yêu cầu kết bạn")
//    @GetMapping("/{idParent}/{idChild}/add-friend")
//    public ResponseEntity<?> requestAddFriend(
//            @PathVariable("idParent") Long idParent,
//            @PathVariable("idChild") Long idChild
//    ) {
//        return VsResponseUtil.ok(userService.requestAddFriend(idParent, idChild));
//    }
//
//    @ApiOperation(value = "Xác nhận kết bạn")
//    @GetMapping("/{idParent}/{idChild}/accept-friend")
//    public ResponseEntity<?> acceptAddFriend(
//            @PathVariable("idParent") Long idParent,
//            @PathVariable("idChild") Long idChild
//    ) {
//        System.out.println(idParent);
//        System.out.println(idChild);
//
//        return VsResponseUtil.ok(userService.acceptAddFriend(idParent, idChild));
//    }
//
//    @ApiOperation(value = "Xem danh sách bạn bè")
//    @GetMapping("/{idParent}/friends")
//    public ResponseEntity<?> acceptAddFriend(
//            @PathVariable("idParent") Long idParent
//    ) {
//        return VsResponseUtil.ok(userService.getAllUserIsFriend(idParent));
//    }


}
