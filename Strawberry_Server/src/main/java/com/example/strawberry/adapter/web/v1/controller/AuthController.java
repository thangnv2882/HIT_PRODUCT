package com.example.strawberry.adapter.web.v1.controller;

import com.example.strawberry.adapter.web.base.VsResponseUtil;
import com.example.strawberry.application.service.IUserService;
import com.example.strawberry.domain.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final IUserService userService;

    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        return VsResponseUtil.ok(userService.login(userDTO));
    }
}
