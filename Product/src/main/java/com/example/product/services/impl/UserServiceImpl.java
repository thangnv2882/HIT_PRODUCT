package com.example.product.services.impl;

import com.example.product.daos.User;
import com.example.product.dtos.ActiveDTO;
import com.example.product.dtos.ResetPasswordDTO;
import com.example.product.dtos.UserDTO;
import com.example.product.exceptions.DuplicateException;
import com.example.product.exceptions.ExceptionAll;
import com.example.product.exceptions.NotFoundException;
import com.example.product.repositories.IUserRepository;
import com.example.product.services.ISendMailService;
import com.example.product.services.IUserService;
import com.example.product.utils.Constants;
import com.example.product.utils.UploadFile;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ISendMailService sendMailService;

    @Autowired
    private UploadFile uploadFile;





    @Override
    public List<User> findAllUsers() {
        System.out.println(userRepository.findAllByStatusIs(Boolean.TRUE).get(0));
        return userRepository.findAllByStatusIs(Boolean.TRUE);
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        return user.get();
    }

    @Override
    public User login(UserDTO userDTO) {
        List<User> users = userRepository.findAllByStatusIs(Boolean.TRUE);
        for (User user: users) {
            if(user.getUsername().compareTo(userDTO.getUsername()) == 0
            && user.getPassword().compareTo(userDTO.getPassword()) == 0) {
                return user;
            }
        }
//        if (users.contains(userDTO)) {
//            User user = modelMapper.map(userDTO, User.class);
//            return user;
//        }
        throw new NotFoundException("Incorrect username or password");
    }

    @Override
    public User registerUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
//        if(userRepository.findByUsername(user.getUsername()) != null) {
//            throw new DuplicateException("Username: " + user.getUsername() + " is already registered.");
//        }
//        if(userRepository.findByEmail(user.getEmail()) != null) {
//            throw new DuplicateException("Email: " + user.getEmail() + " is already registered.");
//        }
        checkEmailOrUsernameExists(user);
//        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        RandomStringUtils rand = new RandomStringUtils();
        String code = rand.randomNumeric(4);
        user.setCodeActive(code);
        String content = Constants.CONTENT
                + "\nThis is your account information:"
                + "\n\tUsername: " + userDTO.getUsername()
                + ".\n\tEmail: " + userDTO.getEmail()
                + ".\n\tPassword: " + userDTO.getPassword()
                + ".\n\nYOUR ACTIVATION CODE: " + code
                + ".\nThank you for using our service.";
        sendMailService.sendMailWithText(Constants.TITLE_ACTIVE, content, userDTO.getEmail());
        userRepository.save(user);
        return user;
    }

    @Override
    public User resendCode(Long id) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        RandomStringUtils rand = new RandomStringUtils();
        String code = rand.randomNumeric(4);
        user.get().setCodeActive(code);
        String content = ".YOUR ACTIVATION CODE: " + code
                + ".\nThank you for using our service.";
        sendMailService.sendMailWithText(Constants.TITLE_ACTIVE, content, user.get().getEmail());
        userRepository.save(user.get());

        return user.get();
    }

    @Override
    public User forgetPassword(Long id) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        RandomStringUtils rand = new RandomStringUtils();
        String code = rand.randomNumeric(4);
        user.get().setCodeActive(code);
        String content = "YOUR SECURITY CODE: " + code
                + ".\nThank you for using our service.";
        sendMailService.sendMailWithText(Constants.TITLE_RESET, content, user.get().getEmail());
        userRepository.save(user.get());
        return user.get();
    }

    @Override
    public User resetPassword(Long id, ResetPasswordDTO resetPasswordDTO) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        if(user.get().getCodeActive().compareTo(resetPasswordDTO.getCode()) == 0) {
            user.get().setCodeActive("");
            user.get().setPassword(resetPasswordDTO.getNewPassword());
            userRepository.save(user.get());
            return user.get();
        }
        throw new ExceptionAll("ACTIVATION CODE is incorrect.");
    }

    @Override
    public User activeUser(Long id, ActiveDTO activeDTO) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        System.out.println(activeDTO.getCodeActive());
        if(user.get().getCodeActive().compareTo(activeDTO.getCodeActive()) == 0) {
            user.get().setStatus(true);
            user.get().setCodeActive("");
            userRepository.save(user.get());
            return user.get();
        }
        throw new ExceptionAll("ACTIVATION CODE is incorrect.");
    }

    @Override
    public User updateUserById(Long id, UserDTO userDTO) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        if(userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new DuplicateException("Username: " + userDTO.getUsername() + " is already registered.");
        }
        if(userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new DuplicateException("Email: " + userDTO.getEmail() + " is already registered.");
        }
        modelMapper.map(userDTO, user.get());
//        user.get().setPassword(passwordEncoder.encode(userDTO.getPassword()));
//        user.get().setPassword(userDTO.getPassword());
        return userRepository.save(user.get());
    }

    @Override
    public User deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        userRepository.delete(user.get());
        return user.get();
    }

    @Override
    public String updateAvatarById(Long id, MultipartFile avatar) throws IOException {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        if(user.get().getLinkAvt() != null) {
            uploadFile.removeImageFromUrl(user.get().getLinkAvt());
        }
        user.get().setLinkAvt(uploadFile.getUrlFromFile(avatar));
        userRepository.save(user.get());

        return "Change successfully";
    }

    public void checkUserExists(Optional<User> user) {
        if (user.isEmpty()) {
            throw new NotFoundException("Couldn't find a user.");
        }
    }
    public void checkEmailOrUsernameExists(User user) {
        if(userRepository.findByUsername(user.getUsername()) != null) {
            throw new DuplicateException("Username: " + user.getUsername() + " is already registered.");
        }
        if(userRepository.findByEmail(user.getEmail()) != null) {
            throw new DuplicateException("Email: " + user.getEmail() + " is already registered.");
        }
    }
}
