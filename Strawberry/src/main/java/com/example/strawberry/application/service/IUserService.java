package com.example.strawberry.application.service;

import com.example.strawberry.domain.dto.ActiveDTO;
import com.example.strawberry.domain.dto.ResetPasswordDTO;
import com.example.strawberry.domain.dto.UserDTO;
import com.example.strawberry.domain.entity.Post;
import com.example.strawberry.domain.entity.User;
import com.example.strawberry.domain.entity.UserRegister;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IUserService {
    User findUserById(Long id);
    User login(UserDTO userDTO);
    List<User> findAllUsers();
    UserRegister registerUser(UserDTO userDTO);
    UserRegister activeUser(Long id, ActiveDTO activeDTO);
    UserRegister resendCode(Long id);
    User forgetPassword(Long id);
    User resetPassword(Long id, ResetPasswordDTO resetPasswordDTO);
//    User activeUser(Long id, ActiveDTO activeDTO);
    User updateUserById(Long id, UserDTO userDTO);
    User deleteUserById(Long id);
    String updateAvatarById(Long id, MultipartFile avatar) throws IOException;

    Set<Post> getAllPostById(Long id);
}
