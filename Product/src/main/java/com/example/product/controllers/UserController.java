package com.example.product.controllers;

import com.example.product.base.BaseController;
import com.example.product.daos.User;
import com.example.product.dtos.ActiveDTO;
import com.example.product.dtos.UserDTO;
import com.example.product.services.IUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseController<User> {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "Xem tài khoản theo id.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return this.resSuccess(userService.findUserById(id));
    }

    @ApiOperation(value = "Xem danh sách tất cả tài khoản.")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return this.resListSuccess(userService.findAllUsers());
    }


    @ApiOperation(value = "Đăng ký tài khoản.")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        return this.resSuccess(userService.registerUser(userDTO));
    }

    @ApiOperation(value = "Kích hoạt tài khoản.")
    @PostMapping("/{id}/active")
    public ResponseEntity<?> activeUser(@PathVariable Long id, @RequestBody ActiveDTO activeDTO) {
        return ResponseEntity.status(200).body(userService.activeUser(id, activeDTO));
    }

    @ApiOperation(value = "Cập nhật thông tin tài khoản.")
    @PatchMapping("/{id}")
    public ResponseEntity<?> editUserById(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return this.resSuccess(userService.updateUserById(id, userDTO));
    }

    @ApiOperation(value = "Xoá tài khoản.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        return this.resSuccess(userService.deleteUserById(id));
    }

    @PostMapping("/{id}/avatar")
    @ApiOperation(value = "Cập nhật ảnh đại diện.")
    public String updateAvatarById(
            @PathVariable("id") Long id,
            @RequestParam(name = "avatar", required = false)MultipartFile avatar) throws IOException {
        return userService.updateAvatarById(id, avatar);
    }

}
