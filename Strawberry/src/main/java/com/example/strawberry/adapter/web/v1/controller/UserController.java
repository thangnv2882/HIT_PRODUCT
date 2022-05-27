package com.example.strawberry.adapter.web.v1.controller;

import com.example.strawberry.adapter.web.base.VsResponseUtil;
import com.example.strawberry.application.service.IUserService;
import com.example.strawberry.domain.dto.ActiveDTO;
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
    @Autowired
    private IUserService userService;

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
    @PostMapping("/register/{id}/active")
    public ResponseEntity<?> activeUser(@PathVariable Long id, @RequestBody ActiveDTO activeDTO) {
        return VsResponseUtil.ok(userService.activeUser(id, activeDTO));
//        return ResponseEntity.status(200).body(userService.activeUser(id, activeDTO));
    }

    @ApiOperation(value = "Gửi lại code.")
    @GetMapping("/register/{id}/resend-code")
    public ResponseEntity<?> registerUser(@PathVariable("id") Long id) {
        return VsResponseUtil.ok(userService.resendCode(id));
    }

    @ApiOperation(value = "Quên mật khẩu.")
    @GetMapping("/{id}/forget-password")
    public ResponseEntity<?> forgetPassword(@PathVariable("id") Long id) {
        return VsResponseUtil.ok(userService.forgetPassword(id));
    }

    @ApiOperation(value = "Đặt lại mật khẩu.")
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<?> forgetPassword(
            @PathVariable("id") Long id,
            @RequestBody ResetPasswordDTO resetPasswordDTO) {
        return VsResponseUtil.ok(userService.resetPassword(id, resetPasswordDTO));
    }

    @ApiOperation(value = "Cập nhật thông tin tài khoản.")
    @PatchMapping("/{id}")
    public ResponseEntity<?> editUserById(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return VsResponseUtil.ok(userService.updateUserById(id, userDTO));
    }

    @ApiOperation(value = "Xoá tài khoản.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        return VsResponseUtil.ok(userService.deleteUserById(id));
    }

    @ApiOperation(value = "Cập nhật ảnh đại diện.")
    @PostMapping("/{id}/avatar")
    public String updateAvatarById(
            @PathVariable("id") Long id,
            @RequestParam(name = "avatar", required = false) MultipartFile avatar) throws IOException {
        return userService.updateAvatarById(id, avatar);
    }

    @ApiOperation(value = "Lấy ra tất cả bài viết của user theo id.")
    @GetMapping("/{id}/posts")
    public ResponseEntity<?> getAllPostById(@PathVariable Long id) {
        return VsResponseUtil.ok(userService.getAllPostById(id));
    }

}
