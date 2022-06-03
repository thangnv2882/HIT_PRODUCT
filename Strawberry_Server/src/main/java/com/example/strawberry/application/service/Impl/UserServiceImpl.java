package com.example.strawberry.application.service.Impl;

import com.example.strawberry.application.constants.CommonConstant;
import com.example.strawberry.application.constants.EmailConstant;
import com.example.strawberry.application.constants.MessageConstant;
//import com.example.strawberry.application.dai.IFriendShipRepository;
import com.example.strawberry.application.dai.IUserRegisterRepository;
import com.example.strawberry.application.dai.IUserRepository;
import com.example.strawberry.application.service.ISendMailService;
import com.example.strawberry.application.service.IUserService;
import com.example.strawberry.application.utils.UploadFile;
import com.example.strawberry.config.exception.DuplicateException;
import com.example.strawberry.config.exception.ExceptionAll;
import com.example.strawberry.config.exception.NotFoundException;
//import com.example.strawberry.domain.dto.ResetPasswordDTO;
import com.example.strawberry.domain.dto.ResetPasswordDTO;
import com.example.strawberry.domain.dto.UserDTO;
import com.example.strawberry.domain.entity.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService {

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    private final IUserRepository userRepository;
    private final IUserRegisterRepository userRegisterRepository;
    private final ModelMapper modelMapper;
    private final ISendMailService sendMailService;
    private final UploadFile uploadFile;

    public UserServiceImpl(IUserRepository userRepository, IUserRegisterRepository userRegisterRepository, ModelMapper modelMapper, ISendMailService sendMailService, UploadFile uploadFile) {
        this.userRepository = userRepository;
        this.userRegisterRepository = userRegisterRepository;
        this.modelMapper = modelMapper;
        this.sendMailService = sendMailService;
        this.uploadFile = uploadFile;
    }


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
        if (!isEmailOrPhoneNumberExists(userRegister)) {
            RandomStringUtils rand = new RandomStringUtils();
            String code = rand.randomNumeric(4);
            userRegister.setCode(code);
            String content = EmailConstant.CONTENT
                    + "\nThis is your account information:"
                    + ".\n\tEmail: " + userDTO.getEmail()
                    + ".\n\tPassword: " + userDTO.getPassword()
                    + ".\n\nYOUR ACTIVATION CODE: " + code
                    + ".\nThank you for using our service.";
            sendMailService.sendMailWithText(EmailConstant.SUBJECT_ACTIVE, content, userDTO.getEmail());
            userRegister.setFullName(userDTO.getFirstName() + " " + userDTO.getLastName());
            userRegisterRepository.save(userRegister);
        }
        UserRegister userRegister1 = userRegisterRepository.findByEmailOrPhoneNumber(userDTO.getEmail(), userDTO.getPhoneNumber());
        return userRegister1;
    }

    @Override
    public User activeUser(Long id, String code) {
        Optional<UserRegister> userRegister = userRegisterRepository.findById(id);
        checkUserRegisterExists(userRegister);
        if (userRegister.get().getCode().compareTo(code) == 0) {
            userRegister.get().setStatus(true);
            userRegister.get().setCode(null);
            User user = modelMapper.map(userRegister.get(), User.class);
            user.setLinkAvt(CommonConstant.AVATAR_DEFAULT);
            user.setId(null);
            userRepository.save(user);
            return user;
        }
        throw new ExceptionAll(MessageConstant.ACTIVE_FALSE);
    }

    @Override
    public UserRegister resendCode(Long id) {
        Optional<UserRegister> userRegister = userRegisterRepository.findById(id);
        checkUserRegisterExists(userRegister);
//        RandomStringUtils rand = new RandomStringUtils();
//        String code = rand.randomNumeric(4);
//        userRegister.get().setCode(code);
        String content = ".YOUR ACTIVATION CODE: " + userRegister.get().getCode()
                + ".\nThank you for using our service.";
        sendMailService.sendMailWithText(EmailConstant.SUBJECT_ACTIVE, content, userRegister.get().getEmail());
        userRegisterRepository.save(userRegister.get());
        return userRegister.get();
    }

    @Override
    public User forgetPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_EXISTS);
        }
        String content = "YOUR PASSWORD: " + user.getPassword()
                + ".\nThank you for using our service.";
        sendMailService.sendMailWithText(EmailConstant.SUBJECT_NOTIFICATION, content, email);
//        userRepository.save(user);
        return user;
    }

    @Override
    public User changePassword(Long id, ResetPasswordDTO resetPasswordDTO) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        if (user.get().getPassword().compareTo(resetPasswordDTO.getOldPassword()) != 0) {
            throw new ExceptionAll("Incorrect password");
        }
        user.get().setPassword(resetPasswordDTO.getNewPassword());
        return user.get();
    }

//    @Override
//    public User resetPassword(ResetPasswordDTO resetPasswordDTO) {
//        User user = userRepository.findByEmail(resetPasswordDTO.getEmail());
//        if(user == null) {
//            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_EXISTS);
//        }
//        if (user.getCode().compareTo(resetPasswordDTO.getCode()) == 0) {
//            user.setCode(null);
//            user.setPassword(resetPasswordDTO.getNewPassword());
//            userRepository.save(user);
//            return user;
//        }
//        throw new ExceptionAll(MessageConstant.ACTIVE_FALSE);
//    }

    @Override
    public User updateUserById(Long id, UserDTO userDTO) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        UserRegister userRegister = modelMapper.map(userDTO, UserRegister.class);
        if (isEmailOrPhoneNumberExists(userRegister)) {
            throw new DuplicateException("Thông tin đã tồn tại.");
        }
        modelMapper.map(userDTO, user.get());
//        user.get().setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user.get());
    }

    @Override
    public User deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        Optional<UserRegister> userRegister = userRegisterRepository.findById(id);
        checkUserRegisterExists(userRegister);
        userRegister.get().setStatus(Boolean.FALSE);
        userRepository.delete(user.get());
        return user.get();
    }

    @Override
    public User updateAvatarById(Long id, MultipartFile avatar) throws IOException {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        if (user.get().getLinkAvt() != null) {
            uploadFile.removeImageFromUrl(user.get().getLinkAvt());
        }
        user.get().setLinkAvt(uploadFile.getUrlFromFile(avatar));
        userRepository.save(user.get());

        return user.get();
    }

    @Override
    public Set<Post> getAllPostById(Long id) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        Set<Post> posts = user.get().getPosts();
        return getAllPostNotInGroup(posts);
    }

    @Override
    public Set<Post> getAllPostByAccess(Long idUser, int access) {
        Optional<User> user = userRepository.findById(idUser);
        checkUserExists(user);
        Set<Post> posts = user.get().getPosts();
        Set<Post> postsEnd = new HashSet<>();
        posts.forEach(post -> {
            if (post.getAccess() == access) {
                postsEnd.add(post);
            }
        });
        return getAllPostNotInGroup(postsEnd);
    }

    @Override
    public Set<Group> getAllGroupByIdUser(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        checkUserExists(user);
        Set<Group> groups = user.get().getGroups();
        return groups;
    }

    public void checkUserExists(Optional<User> user) {
        if (user.isEmpty()) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_EXISTS);
        }
    }

    public Boolean isEmailOrPhoneNumberExists(UserRegister userRegister) {
        List<UserRegister> userRegisters = userRegisterRepository.findAll();
        final int[] d = {0};
        userRegisters.forEach(user -> {
            if (user.getStatus() == Boolean.TRUE) {
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
                if (user.getEmail().compareTo(userRegister.getEmail()) == 0
                        || user.getPhoneNumber().compareTo(userRegister.getPhoneNumber()) == 0) {
                    d[0]++;
                }
            }
        });
        if (d[0] != 0) {
            return true;
        }
        return false;
    }

    public void checkUserRegisterExists(Optional<UserRegister> userRegister) {
        if (userRegister.isEmpty()) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_EXISTS);
        }
    }

    public Set<Post> getAllPostNotInGroup(Set<Post> posts) {
        Set<Post> postEnd = new HashSet<>();
        posts.forEach(i -> {
            if (i.getGroup() == null) {
                postEnd.add(i);
            }
        });
        return postEnd;
    }

}
