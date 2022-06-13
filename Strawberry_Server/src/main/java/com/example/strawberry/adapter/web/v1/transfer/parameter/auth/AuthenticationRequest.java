package com.example.strawberry.adapter.web.v1.transfer.parameter.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthenticationRequest {
    private Long id;
    private String email;
    private String phoneNumber;
    private String password;
}
