package com.example.product.services;

import com.example.product.daos.User;
import com.example.product.dtos.ActiveDTO;
import com.example.product.dtos.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUserService {
    User findUserById(Long id);
    List<User> findAllUsers();
    User registerUser(UserDTO userDTO);
    String activeUser(Long id, ActiveDTO activeDTO);
    User updateUserById(Long id, UserDTO userDTO);
    User deleteUserById(Long id);
    String updateAvatarById(Long id, MultipartFile avatar) throws IOException;

}
