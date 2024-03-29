package com.example.strawberry.application.service.Impl;

import com.example.strawberry.adapter.web.base.AccessType;
import com.example.strawberry.adapter.web.v1.transfer.parameter.auth.AuthenticationRequest;
import com.example.strawberry.adapter.web.v1.transfer.response.AuthenticationResponse;
import com.example.strawberry.application.constants.CommonConstant;
import com.example.strawberry.application.constants.EmailConstant;
import com.example.strawberry.application.constants.MessageConstant;
import com.example.strawberry.application.dai.*;
import com.example.strawberry.application.service.IPostService;
import com.example.strawberry.application.service.ISendMailService;
import com.example.strawberry.application.service.IUserService;
import com.example.strawberry.application.utils.JwtTokenUtil;
import com.example.strawberry.application.utils.UploadFile;
import com.example.strawberry.config.exception.DuplicateException;
import com.example.strawberry.config.exception.ExceptionAll;
import com.example.strawberry.config.exception.NotFoundException;
import com.example.strawberry.domain.dto.ResetPasswordDTO;
import com.example.strawberry.domain.dto.UserDTO;
import com.example.strawberry.domain.entity.*;
import com.google.common.base.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IUserRegisterRepository userRegisterRepository;
    private final PostServiceImpl postService;
    private final ModelMapper modelMapper;
    private final ISendMailService sendMailService;
    private final UploadFile uploadFile;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
//    private final IReactionRepository reactionRepository;


    public UserServiceImpl(IUserRepository userRepository, IUserRegisterRepository userRegisterRepository, PostServiceImpl postService1, ModelMapper modelMapper, ISendMailService sendMailService, UploadFile uploadFile, AuthenticationManager authenticationManager, MyUserDetailsService myUserDetailsService, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRegisterRepository = userRegisterRepository;
        this.postService = postService1;
        this.modelMapper = modelMapper;
        this.sendMailService = sendMailService;
        this.uploadFile = uploadFile;
        this.authenticationManager = authenticationManager;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
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
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        checkUserExists(Optional.ofNullable(user));
        return user;
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(), authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password");
        }

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        String jwt = jwtTokenUtil.generateToken(userDetails);
        User user = userRepository.findByEmail(authenticationRequest.getEmail());
        return new AuthenticationResponse(user, jwt);
    }

    @Override
    public UserRegister registerUser(UserDTO userDTO) {
        UserRegister userRegister = modelMapper.map(userDTO, UserRegister.class);

        Random random = new Random();
        String rand = Integer.toString(random.nextInt(9999));
        // Chuỗi có 4 ký tự
        String code = Strings.padStart(rand, 4, '0');
        String content = EmailConstant.CONTENT
                + "\nThis is your account information:"
                + "\n\tEmail: " + userDTO.getEmail()
                + ".\n\tPassword: " + userDTO.getPassword()
                + ".\n\nYOUR ACTIVATION CODE: " + code
                + ".\nThank you for using our service.";
        if (isEmailExists(userRegister)) {
            userRegister = userRegisterRepository.findByEmail(userDTO.getEmail());
        }
        modelMapper.map(userDTO, userRegister);
        userRegister.setCode(code);
        userRegister.setFullName(userDTO.getFirstName() + " " + userDTO.getLastName());
        userRegisterRepository.save(userRegister);
        sendMailService.sendMailWithText(EmailConstant.SUBJECT_ACTIVE, content, userDTO.getEmail());
        return userRegister;
    }

    @Override
    public User activeUser(Long id, String code) {
        Optional<UserRegister> userRegister = userRegisterRepository.findById(id);
        checkUserRegisterExists(userRegister);
        if (userRegister.get().getCode().compareTo(code) == 0) {
            userRegister.get().setStatus(true);
            userRegister.get().setCode(null);
            User user = modelMapper.map(userRegister.get(), User.class);
            user.setIdUser(null);
            String password = passwordEncoder.encode(userRegister.get().getPassword());
            user.setPassword(password);
            user.setLinkAvt(CommonConstant.AVATAR_DEFAULT);
            user.setLinkCover(CommonConstant.COVER_DEFAULT);
            userRepository.save(user);
            return user;
        }
        throw new ExceptionAll(MessageConstant.ACTIVE_FALSE);
    }

    @Override
    public UserRegister resendCode(Long id) {
        Optional<UserRegister> userRegister = userRegisterRepository.findById(id);
        checkUserRegisterExists(userRegister);
        String content = ".YOUR ACTIVATION CODE: " + userRegister.get().getCode()
                + ".\nThank you for using our service.";
        sendMailService.sendMailWithText(EmailConstant.SUBJECT_ACTIVE, content, userRegister.get().getEmail());
        userRegisterRepository.save(userRegister.get());
        return userRegister.get();
    }

    @Override
    public String forgetPassword(String email) {
        UserRegister user = userRegisterRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_EXISTS);
        }

        String content = "YOUR PASSWORD: " + user.getPassword()
                + ".\nThank you for using our service.";
        sendMailService.sendMailWithText(EmailConstant.SUBJECT_NOTIFICATION, content, email);
        return EmailConstant.SENT_SUCCESSFULLY;
    }

    @Override
    public User changePassword(Long id, ResetPasswordDTO resetPasswordDTO) {
        Optional<User> user = userRepository.findById(id);
        UserRegister userRegister = userRegisterRepository.findByEmail(user.get().getEmail());
        checkUserExists(user);
        checkUserRegisterExists(Optional.ofNullable(userRegister));
        if (passwordEncoder.matches(resetPasswordDTO.getOldPassword(), user.get().getPassword()) == false) {
            throw new ExceptionAll("Incorrect password");
        }
        String password = passwordEncoder.encode(resetPasswordDTO.getNewPassword());
        user.get().setPassword(password);
        userRegister.setPassword(resetPasswordDTO.getNewPassword());
        userRepository.save(user.get());
        userRegisterRepository.save(userRegister);
        return user.get();
    }

    @Override
    public User updateUserById(Long id, UserDTO userDTO) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        UserRegister userRegisterOriginal = userRegisterRepository.findByEmail(user.get().getEmail());
        UserRegister userRegisterNew = modelMapper.map(userDTO, UserRegister.class);
        if (isEmailExists(userRegisterNew)) {
            throw new DuplicateException("Thông tin đã tồn tại.");
        }
        modelMapper.map(userDTO, user.get());
        modelMapper.map(userRegisterNew, userRegisterOriginal);
        userRegisterOriginal.setStatus(Boolean.TRUE);

        String password = passwordEncoder.encode(userDTO.getPassword());
        user.get().setPassword(password);

        userRegisterRepository.save(userRegisterOriginal);
        userRepository.save(user.get());
        return user.get();
    }

    @Override
    public User deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        UserRegister userRegister = userRegisterRepository.findByEmail(user.get().getEmail());
        checkUserRegisterExists(Optional.ofNullable(userRegister));
        userRepository.delete(user.get());
        userRegisterRepository.delete(userRegister);
        return user.get();
    }

    @Override
    public User updateAvatarById(Long id, String linkAvt) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        user.get().setLinkAvt(linkAvt);
        userRepository.save(user.get());

        return user.get();
    }

    @Override
    public User updateCoverById(Long id, String linkCover) {
        Optional<User> user = userRepository.findById(id);
        checkUserExists(user);
        user.get().setLinkCover(linkCover);
        userRepository.save(user.get());
        return user.get();
    }

    @Override
    public List<?> getAllPostByIdUser(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        checkUserExists(user);
        Set<Post> posts = user.get().getPosts();
        return postService.getAllPostNotInGroup(posts);
    }

    @Override
    public List<?> getAllPostByIdUserAndAccess(Long idUser, AccessType access) {
        Optional<User> user = userRepository.findById(idUser);
        checkUserExists(user);
        Set<Post> posts = user.get().getPosts();
        Set<Post> postsEnd = new HashSet<>();
        posts.forEach(post -> {
            if (post.getAccess().equals(access)) {
                postsEnd.add(post);
            }
        });
        return postService.getAllPostNotInGroup(postsEnd);
    }

    @Override
    public Set<Group> getAllGroupByIdUser(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        checkUserExists(user);

        Set<UserGroup> userGroups = user.get().getUserGroups();
        Set<Group> groups = new HashSet<>();
        userGroups.forEach(i -> {
            groups.add(i.getGroup());
        });
        return groups;
    }

    @Override
    public Set<Image> getAllImageByIdUser(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        checkUserExists(user);
        Set<Post> posts = user.get().getPosts();
        Set<Image> images = new HashSet<>();

        posts.forEach(post -> {
            post.getImages().forEach(image -> {
                images.add(image);
            });
        });
        return images;
    }

    @Override
    public Set<Video> getAllVideoByIdUser(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        checkUserExists(user);
        Set<Post> posts = user.get().getPosts();
        Set<Video> videos = new HashSet<>();
        posts.forEach(post -> {
            post.getVideos().forEach(video -> {
                videos.add(video);
            });
        });
        return videos;
    }

    @Override
    public List<UserRegister> findAllUserRegister() {
        List<UserRegister> userRegisters = userRegisterRepository.findAll();
        return userRegisters;
    }

    public static void checkUserExists(Optional<User> user) {
        if (user.isEmpty()) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_EXISTS);
        }
    }

    public Boolean isEmailExists(UserRegister userRegister) {
        List<UserRegister> userRegisters = userRegisterRepository.findAll();
        for (UserRegister user : userRegisters) {
            if (user.getEmail().compareTo(userRegister.getEmail()) == 0) {
                if (user.getStatus() == Boolean.TRUE) {
                    throw new DuplicateException("Email: " + userRegister.getEmail() + " is already registered.");
                }
                return true;
            }
        }
        return false;
    }

    public void checkUserRegisterExists(Optional<UserRegister> userRegister) {
        if (userRegister.isEmpty()) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_EXISTS);
        }
    }
}
