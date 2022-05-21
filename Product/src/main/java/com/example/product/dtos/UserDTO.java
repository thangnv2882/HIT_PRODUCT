package com.example.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String gender;
    private String phoneNumber;
    private String address;
    private String birthday;
    private String biography;
    private String linkAvt;
}
