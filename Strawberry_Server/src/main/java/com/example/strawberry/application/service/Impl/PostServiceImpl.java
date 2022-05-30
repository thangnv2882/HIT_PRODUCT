package com.example.strawberry.application.service.Impl;

import com.example.strawberry.application.constants.MessageConstant;
import com.example.strawberry.application.dai.*;
import com.example.strawberry.application.service.IPostService;
import com.example.strawberry.application.utils.UploadFile;
import com.example.strawberry.config.exception.NotFoundException;
import com.example.strawberry.domain.dto.PostDTO;
import com.example.strawberry.domain.entity.*;
import com.github.slugify.Slugify;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class PostServiceImpl implements IPostService {

    private final IPostRepository postRepository;
    private final IUserRepository userRepository;
    private final IImageRepository imageRepository;
    private final IVideoRepository videoRepository;
    private final IReactionRepository reactionRepository;
    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;
    private final UploadFile uploadFile;
    private Slugify slg = new Slugify();

    public PostServiceImpl(IPostRepository postRepository, IUserRepository userRepository, IImageRepository imageRepository, IVideoRepository videoRepository, IReactionRepository reactionRepository, UserServiceImpl userService, ModelMapper modelMapper, UploadFile uploadFile) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.videoRepository = videoRepository;
        this.reactionRepository = reactionRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.uploadFile = uploadFile;
    }

    @Override
    public Post createPost(Long idUser, PostDTO postDTO, MultipartFile[] fileImages, MultipartFile[] fileVideos) {
        Optional<User> user = userRepository.findById(idUser);
        userService.checkUserExists(user);
        Post post = modelMapper.map(postDTO, Post.class);
        post.setUser(user.get());
//        post.setSlugPost(slg.slugify(postDTO.getContentPost()));

        setMediaToPost(post, fileImages, fileVideos);
        postRepository.save(post);
        return post;
    }

    @Override
    public Post updatePost(Long id, PostDTO postDTO, MultipartFile[] fileImages, MultipartFile[] fileVideos) {
        Optional<Post> post = postRepository.findById(id);
        checkPostExists(post);
        modelMapper.map(postDTO, post.get());
//        post.get().setSlugPost(slg.slugify(postDTO.getContentPost()));
        setMediaToPost(post.get(), fileImages, fileVideos);

        postRepository.save(post.get());
        return post.get();
    }

    @Override
    public Post deletePostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        checkPostExists(post);
        postRepository.delete(post.get());
        return post.get();
    }


    @Override
    public Set<Image> getAllImageById(Long id) {
        Set<Image> images = postRepository.findById(id).get().getImages();
        return images;
    }

    @Override
    public Set<Video> getAllVideoById(Long id) {
        Set<Video> videos = postRepository.findById(id).get().getVideos();
        return videos;
    }

    @Override
    public Reaction setReactByIdPost(Long idPost, Long idReation) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        Set<Reaction> reactions = post.get().getReactions();
        Optional<Reaction> reaction = reactionRepository.findById(idReation);

//
        reactions.add(reaction.get());
        post.get().setReactions(reactions);
        reactions.forEach(i -> {
            System.out.println(i.getId());
        });
//        postRepository.save(post.get());
//        return reaction.get();
        return null;
    }

    @Override
    public Set<Reaction> getAllReactionByIdPost(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        Set<Reaction> reactions = post.get().getReactions();
        return reactions;
    }

    @Override
    public Set<Comment> getAllCommentByIdPost(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        Set<Comment> comments = post.get().getComments();
        return comments;
    }



    public void checkPostExists(Optional<Post> post) {
        if (post.isEmpty()) {
            throw new NotFoundException(MessageConstant.POST_NOT_EXISTS);
        }
    }

    public static void checkReactionExists(Optional<Reaction> reaction) {
        if (reaction.isEmpty()) {
            throw new NotFoundException(MessageConstant.REACTION_NOT_EXISTS);
        }
    }

    public void setMediaToPost(Post post, MultipartFile[] fileImages, MultipartFile[] fileVideos) {
        if (fileImages != null) {
            Set<Image> images = new HashSet<>();

            for (MultipartFile file : fileImages) {
                Image image = new Image();
                image.setLinkImage(uploadFile.getUrlFromFile(file));
                image.setPost(post);
                imageRepository.save(image);
                images.add(image);
            }
            post.setImages(images);
        } else {
            post.setImages(new HashSet<>());
        }
        if (fileVideos != null) {
            Set<Video> videos = new HashSet<>();
            for (MultipartFile file : fileVideos) {
                Video video = new Video();
                try {
                    video.setLinkVideo(uploadFile.getUrlFromLargeFile(file));
                    video.setPost(post);
                    videoRepository.save(video);
                    videos.add(video);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            post.setVideos(videos);
        } else {
            post.setVideos(new HashSet<>());
        }
    }
}
