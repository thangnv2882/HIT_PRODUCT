package com.example.strawberry.application.service.Impl;

import com.example.strawberry.adapter.web.v1.transfer.parameter.auth.AuthenticationRequest;
import com.example.strawberry.adapter.web.v1.transfer.response.AuthenticationResponse;
import com.example.strawberry.application.constants.CommonConstant;
import com.example.strawberry.application.constants.EmailConstant;
import com.example.strawberry.application.constants.MessageConstant;
import com.example.strawberry.application.dai.IPostRepository;
import com.example.strawberry.application.dai.IReactionRepository;
import com.example.strawberry.application.dai.IUserRegisterRepository;
import com.example.strawberry.application.dai.IUserRepository;
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

import static com.example.strawberry.adapter.web.base.ReactionType.*;


@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IUserRegisterRepository userRegisterRepository;
    private final IPostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ISendMailService sendMailService;
    private final UploadFile uploadFile;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final IReactionRepository reactionRepository;


    public UserServiceImpl(IUserRepository userRepository, IUserRegisterRepository userRegisterRepository, IPostRepository postRepository, ModelMapper modelMapper, ISendMailService sendMailService, UploadFile uploadFile, AuthenticationManager authenticationManager, MyUserDetailsService myUserDetailsService, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder, IReactionRepository reactionRepository) {
        this.userRepository = userRepository;
        this.userRegisterRepository = userRegisterRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.sendMailService = sendMailService;
        this.uploadFile = uploadFile;
        this.authenticationManager = authenticationManager;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.reactionRepository = reactionRepository;
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
        return new AuthenticationResponse(user.getIdUser(), authenticationRequest.getEmail(), authenticationRequest.getPhoneNumber(), jwt);
    }

//    @Override
//    public User login(UserDTO userDTO) {
//        User user = userRepository.findByEmailOrPhoneNumber(userDTO.getEmail(), userDTO.getPhoneNumber());
//        if (user.getPassword().compareTo(userDTO.getPassword()) == 0) {
//            return user;
//        }
//        throw new NotFoundException(MessageConstant.ACCOUNT_NOT_EXISTS);
//    }

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
        if (!isEmailOrPhoneNumberExists(userRegister)) {
            sendMailService.sendMailWithText(EmailConstant.SUBJECT_ACTIVE, content, userDTO.getEmail());
            userRegister.setCode(code);
            userRegister.setFullName(userDTO.getFirstName() + " " + userDTO.getLastName());
            userRegister.setLinkAvt(CommonConstant.AVATAR_DEFAULT);
            userRegisterRepository.save(userRegister);
            return userRegister;
        }
        UserRegister userRegister1 = userRegisterRepository.findByEmailOrPhoneNumber(userDTO.getEmail(), userDTO.getPhoneNumber());
        sendMailService.sendMailWithText(EmailConstant.SUBJECT_ACTIVE, content, userDTO.getEmail());
        userRegister1.setCode(code);
        userRegister1.setFullName(userDTO.getFirstName() + " " + userDTO.getLastName());
        userRegister1.setLinkAvt(CommonConstant.AVATAR_DEFAULT);
        userRegisterRepository.save(userRegister1);
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
            user.setIdUser(null);
            String password = passwordEncoder.encode(userRegister.get().getPassword());
            System.out.println(password);
            user.setPassword(password);
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
        UserRegister userRegister = userRegisterRepository.findByEmailOrPhoneNumber(user.get().getEmail(), user.get().getPhoneNumber());
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
        UserRegister userRegisterOriginal = userRegisterRepository.findByEmailOrPhoneNumber(user.get().getEmail(), user.get().getPhoneNumber());
        UserRegister userRegisterNew = modelMapper.map(userDTO, UserRegister.class);
        if (isEmailOrPhoneNumberExists(userRegisterNew)) {
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
        UserRegister userRegister = userRegisterRepository.findByEmailOrPhoneNumber(user.get().getEmail(), user.get().getPhoneNumber());
        checkUserRegisterExists(Optional.ofNullable(userRegister));
        userRepository.delete(user.get());
        userRegisterRepository.delete(userRegister);
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
    public List<?> getAllPostByIdUser(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        checkUserExists(user);
        Set<Post> posts = user.get().getPosts();
        return PostServiceImpl.getAllPostNotInGroup(posts);
    }

    @Override
    public List<?> getAllPostByIdUserAndAccess(Long idUser, int access) {
        Optional<User> user = userRepository.findById(idUser);
        checkUserExists(user);
        Set<Post> posts = user.get().getPosts();
        Set<Post> postsEnd = new HashSet<>();
        posts.forEach(post -> {
            if (post.getAccess() == access) {
                postsEnd.add(post);
            }
        });
        return PostServiceImpl.getAllPostNotInGroup(postsEnd);
    }
//
//    public List<?> getAllPostNotInGroup(Set<Post> posts) {
//        Set<Post> postEnd = new HashSet<>();
//        posts.forEach(i -> {
//            if (i.getGroup() == null) {
//                postEnd.add(i);
//            }
//        });
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (Post post : postEnd) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("id", post.getIdPost());
//            map.put("createdAt", post.getCreatedAt());
//            map.put("updatedAt", post.getUpdatedAt());
//            map.put("contentPost", post.getContentPost());
//            map.put("access", post.getAccess());
//            map.put("user", post.getUser());
//            map.put("reactions", getCountReactionOfPost(post.getIdPost()));
//            map.put("images", getAllImageByIdPost(post.getIdPost()));
//            map.put("videos", getAllVideoByIdPost(post.getIdPost()));
//            map.put("comments", getAllCommentByIdPost(post.getIdPost()));
//            list.add(map);
//        }
//        return list;
//    }
//
//    @Override
//    public Map<String, Long> getCountReactionOfPost(Long idPost) {
//        Map<String, Long> countReaction = new HashMap<>();
//        countReaction.put("LIKE", reactionRepository.countByPostIdPostAndAndReactionType(idPost, LIKE));
//        countReaction.put("LOVE", reactionRepository.countByPostIdPostAndAndReactionType(idPost, LOVE));
//        countReaction.put("CARE", reactionRepository.countByPostIdPostAndAndReactionType(idPost, CARE));
//        countReaction.put("HAHA", reactionRepository.countByPostIdPostAndAndReactionType(idPost, HAHA));
//        countReaction.put("WOW", reactionRepository.countByPostIdPostAndAndReactionType(idPost, WOW));
//        countReaction.put("SAD", reactionRepository.countByPostIdPostAndAndReactionType(idPost, SAD));
//        countReaction.put("ANGRY", reactionRepository.countByPostIdPostAndAndReactionType(idPost, ANGRY));
//        countReaction.put("ALL", reactionRepository.countByPostIdPost(idPost));
//        return countReaction;
//    }
//
//    @Override
//    public Set<Image> getAllImageByIdPost(Long idPost) {
//        Optional<Post> post = postRepository.findById(idPost);
//        PostServiceImpl.checkPostExists(post);
//        Set<Image> images = postRepository.findById(idPost).get().getImages();
//        return images;
//    }
//
//    @Override
//    public Set<Video> getAllVideoByIdPost(Long idPost) {
//        Optional<Post> post = postRepository.findById(idPost);
//        PostServiceImpl.checkPostExists(post);
//        Set<Video> videos = postRepository.findById(idPost).get().getVideos();
//        return videos;
//    }
//
//    @Override
//    public Set<Comment> getAllCommentByIdPost(Long idPost) {
//        Optional<Post> post = postRepository.findById(idPost);
//        PostServiceImpl.checkPostExists(post);
//        Set<Comment> comments = post.get().getComments();
//        return comments;
//    }

    @Override
    public Set<Group> getAllGroupByIdUser(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        checkUserExists(user);
        Set<Group> groups = user.get().getGroups();
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

    public Boolean isEmailOrPhoneNumberExists(UserRegister userRegister) {
        List<UserRegister> userRegisters = userRegisterRepository.findAll();
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
        for(UserRegister user : userRegisters) {
            if (user.getStatus() == Boolean.FALSE) {
                if (user.getEmail().compareTo(userRegister.getEmail()) == 0
                        || user.getPhoneNumber().compareTo(userRegister.getPhoneNumber()) == 0) {
                    return true;
                }
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
