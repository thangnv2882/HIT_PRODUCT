package com.example.strawberry.application.service;

import com.example.strawberry.adapter.web.base.AccessType;
import com.example.strawberry.adapter.web.v1.transfer.parameter.auth.AuthenticationRequest;
import com.example.strawberry.adapter.web.v1.transfer.response.AuthenticationResponse;
import com.example.strawberry.domain.dto.ResetPasswordDTO;
import com.example.strawberry.domain.dto.UserDTO;
import com.example.strawberry.domain.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IUserService {
    User findUserById(Long id);
    User getUserByEmail(String email);

    //    User login(UserDTO userDTO);
    AuthenticationResponse login(AuthenticationRequest request) throws Exception;



    List<User> findAllUsers();
    UserRegister registerUser(UserDTO userDTO);
    UserRegister resendCode(Long id);
    User activeUser(Long id, String code);
    String forgetPassword(String email);
    User changePassword(Long id, ResetPasswordDTO resetPasswordDTO);
    User updateUserById(Long id, UserDTO userDTO);
    User deleteUserById(Long id);
    User updateAvatarById(Long id, MultipartFile avatar) throws IOException;

    List<?> getAllPostByIdUser(Long idUser);
    List<?> getAllPostByIdUserAndAccess(Long idUser, AccessType access);

    Set<Group> getAllGroupByIdUser(Long idUser);
    Set<Image> getAllImageByIdUser(Long idUser);
    Set<Video> getAllVideoByIdUser(Long idUser);

    List<UserRegister> findAllUserRegister();

//    Map<String, Long> getCountReactionOfPost(Long idPost);
//
//    Set<Image> getAllImageByIdPost(Long idPost);
//    Set<Video> getAllVideoByIdPost(Long idPost);
//
//    Set<Comment> getAllCommentByIdPost(Long idPost);
}
