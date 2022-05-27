package com.example.strawberry.application.service.Impl;

import com.example.strawberry.application.constants.EmailConstant;
import com.example.strawberry.application.constants.MessageConstant;
import com.example.strawberry.application.dai.IUserRegisterRepository;
import com.example.strawberry.application.dai.IUserRepository;
import com.example.strawberry.application.service.ISendMailService;
import com.example.strawberry.application.service.IUserService;
import com.example.strawberry.application.utils.UploadFile;
import com.example.strawberry.config.exception.DuplicateException;
import com.example.strawberry.config.exception.ExceptionAll;
import com.example.strawberry.config.exception.NotFoundException;
import com.example.strawberry.domain.dto.ActiveDTO;
import com.example.strawberry.domain.dto.ResetPasswordDTO;
import com.example.strawberry.domain.dto.UserDTO;
import com.example.strawberry.domain.entity.Post;
import com.example.strawberry.domain.entity.User;
import com.example.strawberry.domain.entity.UserRegister;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService {

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserRegisterRepository userRegisterRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ISendMailService sendMailService;

    @Autowired
    private UploadFile uploadFile;


    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        return user.get();
    }

    @Override
    public User login(UserDTO userDTO) {
        User user = userRepository.findByEmailOrPhoneNumber(userDTO.getEmail(), userDTO.getPhoneNumber());
        if (user.getPassword().compareTo(userDTO.getPassword()) == 0) {
            return user;
        }
        throw new NotFoundException(MessageConstant.ACCOUNT_NOT_EXISTS);
    }

    @Override
    public UserRegister registerUser(UserDTO userDTO) {
        UserRegister userRegister = modelMapper.map(userDTO, UserRegister.class);
        if (!isEmailOrUsernameOrPhoneNumberExists(userRegister)) {
            RandomStringUtils rand = new RandomStringUtils();
            String code = rand.randomNumeric(4);
            userRegister.setCode(code);
            String content = EmailConstant.CONTENT
                    + "\nThis is your account information:"
                    + "\n\tUsername: " + userDTO.getUsername()
                    + ".\n\tEmail: " + userDTO.getEmail()
                    + ".\n\tPassword: " + userDTO.getPassword()
                    + ".\n\nYOUR ACTIVATION CODE: " + code
                    + ".\nThank you for using our service.";
            sendMailService.sendMailWithText(EmailConstant.SUBJECT_ACTIVE, content, userDTO.getEmail());
            userRegisterRepository.save(userRegister);
        }
        return userRegister;
    }

    @Override
    public UserRegister activeUser(Long id, ActiveDTO activeDTO) {
        Optional<UserRegister> userRegister = userRegisterRepository.findById(id);
        checkUserRegisterExists(userRegister);
        if (userRegister.get().getCode().compareTo(activeDTO.getCode()) == 0) {
            userRegister.get().setStatus(true);
            userRegister.get().setCode("");
//            userRegister.get().setId(null);
            User user = modelMapper.map(userRegister.get(), User.class);
            user.setId(null);
//            System.out.println(user.getId());
            userRepository.save(user);
            return userRegister.get();
        }
        throw new ExceptionAll(MessageConstant.ACTIVE_FALSE);
    }

    @Override
    public UserRegister resendCode(Long id) {
        Optional<UserRegister> userRegister = userRegisterRepository.findById(id);
        checkUserRegisterExists(userRegister);
        RandomStringUtils rand = new RandomStringUtils();
        String code = rand.randomNumeric(4);
        userRegister.get().setCode(code);
        String content = ".YOUR ACTIVATION CODE: " + code
                + ".\nThank you for using our service.";
        sendMailService.sendMailWithText(EmailConstant.SUBJECT_ACTIVE, content, userRegister.get().getEmail());
        userRegisterRepository.save(userRegister.get());

        return userRegister.get();
    }



//    @Override
//    public User resendCode(Long id) {
//        Optional<User> user = userRepository.findById(id);
//        checkUserExists(user);
//        RandomStringUtils rand = new RandomStringUtils();
//        String code = rand.randomNumeric(4);
//        user.get().setCodeActive(code);
//        String content = ".YOUR ACTIVATION CODE: " + code
//                + ".\nThank you for using our service.";
//        sendMailService.sendMailWithText(EmailConstant.SUBJECT_ACTIVE, content, user.get().getEmail());
//        userRepository.save(user.get());
//
//        return user.get();
//    }

    @Override
    public User forgetPassword(Long id) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        RandomStringUtils rand = new RandomStringUtils();
        String code = rand.randomNumeric(4);
        user.get().setCode(code);
        String content = "YOUR SECURITY CODE: " + code
                + ".\nThank you for using our service.";
        sendMailService.sendMailWithText(EmailConstant.SUBJECT_RESET, content, user.get().getEmail());
        userRepository.save(user.get());
        return user.get();
    }

    @Override
    public User resetPassword(Long id, ResetPasswordDTO resetPasswordDTO) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        if (user.get().getCode().compareTo(resetPasswordDTO.getCode()) == 0) {
            user.get().setCode("");
            user.get().setPassword(resetPasswordDTO.getNewPassword());
            userRepository.save(user.get());
            return user.get();
        }
        throw new ExceptionAll(MessageConstant.ACTIVE_FALSE);
    }

    @Override
    public User updateUserById(Long id, UserDTO userDTO) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new DuplicateException("Username: " + userDTO.getUsername() + " is already registered.");
        }
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
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
    public String updateAvatarById(Long id, MultipartFile avatar) throws IOException{
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        if (user.get().getLinkAvt() != null) {
            uploadFile.removeImageFromUrl(user.get().getLinkAvt());
        }
        user.get().setLinkAvt(uploadFile.getUrlFromFile(avatar));
        userRepository.save(user.get());

        return "Change successfully";
    }

    @Override
    public Set<Post> getAllPostById(Long id) {
        Set<Post> posts = userRepository.findById(id).get().getPosts();
        return posts;
    }

    public static void checkUserExists(Optional<User> user) {
        if (user.isEmpty()) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_EXISTS);
        }
    }

    public Boolean isEmailOrUsernameOrPhoneNumberExists(UserRegister userRegister) {
        List<UserRegister> userRegisters = userRegisterRepository.findAll();
        final int[] d = {0};
        userRegisters.forEach(user -> {
            if (user.getStatus() == Boolean.TRUE) {
                if (user.getUsername().compareTo(userRegister.getUsername()) == 0) {
                    throw new DuplicateException("Username: " + userRegister.getUsername() + " is already registered.");
                }
                if (user.getEmail().compareTo(userRegister.getEmail()) == 0) {
                    throw new DuplicateException("Email: " + userRegister.getEmail() + " is already registered.");
                }
                if (user.getPhoneNumber().compareTo(userRegister.getPhoneNumber()) == 0) {
                    throw new DuplicateException("Phone number: " + userRegister.getPhoneNumber() + " is already registered.");
                }
            }
        });
        userRegisters.forEach(user -> {
            if (user.getStatus() == Boolean.FALSE) {
                if (user.getUsername().compareTo(userRegister.getUsername()) == 0
                        || user.getEmail().compareTo(userRegister.getEmail()) == 0
                        || user.getPhoneNumber().compareTo(userRegister.getPhoneNumber()) == 0) {
                    d[0]++;
                }
            }
        });
        if(d[0] != 0) {
            return true;
        }
        return false;
    }

    public static void checkUserRegisterExists(Optional<UserRegister> userRegister) {
        if (userRegister.isEmpty()) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_EXISTS);
        }
    }
}
