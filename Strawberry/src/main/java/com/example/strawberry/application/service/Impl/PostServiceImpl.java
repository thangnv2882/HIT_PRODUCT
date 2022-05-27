//package com.example.strawberry.application.service.Impl;
//
//import com.example.strawberry.application.constants.MessageConstant;
//import com.example.strawberry.application.dai.IImageRepository;
//import com.example.strawberry.application.dai.IPostRepository;
//import com.example.strawberry.application.dai.IUserRepository;
//import com.example.strawberry.application.service.IPostService;
//import com.example.strawberry.application.utils.UploadFile;
//import com.example.strawberry.config.exception.NotFoundException;
//import com.example.strawberry.domain.dto.PostDTO;
//import com.example.strawberry.domain.entity.Image;
//import com.example.strawberry.domain.entity.Post;
//import com.example.strawberry.domain.entity.User;
//import com.github.slugify.Slugify;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Optional;
//import java.util.Set;
//
//@Service
//public class PostServiceImpl implements IPostService {
//
//    @Autowired
//    private IPostRepository postRepository;
//
//    @Autowired
//    private IUserRepository userRepository;
//
//    @Autowired
//    private ImageServiceImpl imageService;
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Autowired
//    private UploadFile uploadFile;
//
//    private Slugify slg = new Slugify();
//
//    @Override
//    public Post createPost(Long idUser, PostDTO postDTO, MultipartFile[] fileImage) {
//        Optional<User> user = userRepository.findById(idUser);
//        userService.checkUserExists(user);
//        Post post = modelMapper.map(postDTO, Post.class);
//        post.setUser(user.get());
//        post.setSlugPost(slg.slugify(postDTO.getContentPost()));
//        post.setImages();
//
//
//        Image image = new Image();
//        image.setLinkImage(uploadFile.getUrlFromFile(fileImage));
//        imageRepository.save(image);
//
//
//        postRepository.save(post);
//        return post;
//    }
//
//    @Override
//    public Post updatePost(Long id, PostDTO postDTO) {
//        Optional<Post> post = postRepository.findById(id);
//        checkPostExists(post);
//        modelMapper.map(postDTO, post.get());
//        return postRepository.save(post.get());
//    }
//
//    @Override
//    public Post deletePostById(Long id) {
//        Optional<Post> post = postRepository.findById(id);
//        checkPostExists(post);
//        postRepository.delete(post.get());
//        return post.get();
//    }
//
//
//    @Override
//    public Set<Image> getAllImageById(Long id) {
//        Set<Image> images = postRepository.findById(id).get().getImages();
//        return images;
//    }
//
//    public static void checkPostExists(Optional<Post> post) {
//        if (post.isEmpty()) {
//            throw new NotFoundException(MessageConstant.POST_NOT_EXISTS);
//        }
//    }
//}
