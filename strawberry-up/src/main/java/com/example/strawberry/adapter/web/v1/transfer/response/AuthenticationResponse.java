package com.example.strawberry.adapter.web.v1.transfer.response;

import com.example.strawberry.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private User user;
    private String jwt;
}
