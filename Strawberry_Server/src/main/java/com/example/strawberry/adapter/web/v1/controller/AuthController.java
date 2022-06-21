package com.example.strawberry.adapter.web.v1.controller;

import com.example.strawberry.adapter.web.base.RestApiV1;
import com.example.strawberry.adapter.web.base.VsResponseUtil;
import com.example.strawberry.adapter.web.v1.transfer.parameter.auth.AuthenticationRequest;
import com.example.strawberry.application.constants.UrlConstant;
import com.example.strawberry.application.service.IUserService;
import com.example.strawberry.domain.dto.UserDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestApiV1
public class AuthController {

    private final IUserService userService;

    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Đăng nhập.")
    @PostMapping(UrlConstant.Auth.LOGIN)
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return VsResponseUtil.ok(userService.login(authenticationRequest));
    }


}
