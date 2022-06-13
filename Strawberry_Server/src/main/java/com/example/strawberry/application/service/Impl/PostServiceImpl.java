package com.example.strawberry.application.service.Impl;

import com.example.strawberry.application.constants.MessageConstant;
import com.example.strawberry.application.dai.*;
import com.example.strawberry.application.service.IPostService;
import com.example.strawberry.application.utils.UploadFile;
import com.example.strawberry.config.exception.ExceptionAll;
import com.example.strawberry.config.exception.NotFoundException;
import com.example.strawberry.domain.dto.PostDTO;
import com.example.strawberry.domain.entity.*;
import com.github.slugify.Slugify;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.example.strawberry.adapter.web.base.ReactionType.*;

@Service
public class PostServiceImpl implements IPostService {

    private static IPostRepository postRepository;
    private final IUserRepository userRepository;
    private final IGroupRepository groupRepository;
    private final IImageRepository imageRepository;
    private final IVideoRepository videoRepository;
    private static IReactionRepository reactionRepository;
    private final UserServiceImpl userService;
    private final GroupServiceImpl groupService;
    private final ModelMapper modelMapper;
    private final UploadFile uploadFile;
    private Slugify slg = new Slugify();

    public PostServiceImpl(IPostRepository postRepository, IUserRepository userRepository, IGroupRepository groupRepository, IImageRepository imageRepository, IVideoRepository videoRepository, IReactionRepository reactionRepository, UserServiceImpl userService, GroupServiceImpl groupService, ModelMapper modelMapper, UploadFile uploadFile) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.imageRepository = imageRepository;
        this.videoRepository = videoRepository;
        this.reactionRepository = reactionRepository;
        this.userService = userService;
        this.groupService = groupService;
        this.modelMapper = modelMapper;
        this.uploadFile = uploadFile;
    }

    @Override
    public Post createPost(Long idUser, PostDTO postDTO, MultipartFile[] fileImages, MultipartFile[] fileVideos) {
        Optional<User> user = userRepository.findById(idUser);
        UserServiceImpl.checkUserExists(user);
        Post post = modelMapper.map(postDTO, Post.class);
        post.setUser(user.get());
        setMediaToPost(post, fileImages, fileVideos);
        postRepository.save(post);
        return post;
    }

    @Override
    public Post updatePost(Long idUserFix, Long idPost, PostDTO postDTO, MultipartFile[] fileImages, MultipartFile[] fileVideos) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);

        Optional<User> userFix = userRepository.findById(idUserFix);
        UserServiceImpl.checkUserExists(userFix);
        User userOwns = post.get().getUser();
        if(userOwns.getIdUser() != userFix.get().getIdUser()) {
            throw new ExceptionAll("This post is not yours.");
        }

        modelMapper.map(postDTO, post.get());
        setMediaToPost(post.get(), fileImages, fileVideos);
        postRepository.save(post.get());
        return post.get();
    }

    @Override
    public Post deletePostById(Long idUserFix, Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        Optional<User> userFix = userRepository.findById(idUserFix);
        UserServiceImpl.checkUserExists(userFix);
        User userOwns = post.get().getUser();
        if(userOwns.getIdUser() != userFix.get().getIdUser()) {
            throw new ExceptionAll("This post is not yours.");
        }
        postRepository.delete(post.get());
        return post.get();
    }


    @Override
    public List<?> getAllPostPublic(int access) {
        Set<Post> posts = postRepository.findAllByAccess(access);
        return getAllPostNotInGroup(posts);
    }

    public static List<?> getAllPostNotInGroup(Set<Post> posts) {
        Set<Post> postEnd = new HashSet<>();
        posts.forEach(i -> {
            if (i.getGroup() == null) {
                postEnd.add(i);
            }
        });
        List<Map<String, Object>> list = new ArrayList<>();
        for (Post post : postEnd) {
            Map<String, Object> map = new HashMap<>();
            map.put("idPost", post.getIdPost());
            map.put("createdAt", post.getCreatedAt());
            map.put("updatedAt", post.getUpdatedAt());
            map.put("contentPost", post.getContentPost());
            map.put("access", post.getAccess());
            map.put("user", post.getUser());
            map.put("reactions", getCountReactionOfPost(post.getIdPost()));
            map.put("images", getAllImageByIdPostSimple(post.getIdPost()));
            map.put("videos", getAllVideoByIdPostSimple(post.getIdPost()));
            map.put("countComments", countCommentByIdPost(post.getIdPost()));
            list.add(map);
        }

        list.sort((l1, l2) -> ((Long) l2.get("idPost")).compareTo((Long) l1.get("idPost")));

        return list;
    }

//    Hiện thị các lượt bày tỏ cảm xúc của bài post này
    public static Map<String, Long> getCountReactionOfPost(Long idPost) {
        Map<String, Long> countReaction = new HashMap<>();
        countReaction.put("LIKE", reactionRepository.countByPostIdPostAndAndReactionType(idPost, LIKE));
        countReaction.put("LOVE", reactionRepository.countByPostIdPostAndAndReactionType(idPost, LIKE));
        countReaction.put("CARE", reactionRepository.countByPostIdPostAndAndReactionType(idPost, CARE));
        countReaction.put("HAHA", reactionRepository.countByPostIdPostAndAndReactionType(idPost, HAHA));
        countReaction.put("WOW", reactionRepository.countByPostIdPostAndAndReactionType(idPost, WOW));
        countReaction.put("SAD", reactionRepository.countByPostIdPostAndAndReactionType(idPost, SAD));
        countReaction.put("ANGRY", reactionRepository.countByPostIdPostAndAndReactionType(idPost, ANGRY));
        countReaction.put("ALL", reactionRepository.countByPostIdPost(idPost));
        return countReaction;
    }


//    Lấy ra thông tin cơ bản của Video trong Post (idImage, linkImage)
    public static List<Map<String, Object>> getAllImageByIdPostSimple(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        Set<Image> images = postRepository.findById(idPost).get().getImages();
        List<Map<String, Object>> list = new ArrayList<>();
        images.forEach(image -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idImage", image.getIdImage());
            map.put("linkImage", image.getLinkImage());
            list.add(map);
        });

//        Sắp xếp theo id tăng dần
        list.sort((l1, l2) -> ((Long) l1.get("idImage")).compareTo((Long) l2.get("idImage")));

        return list;
    }

//    Lấy ra thông tin cơ bản của Video trong Post (idVideo, linkVideo)
    public static List<Map<String, Object>> getAllVideoByIdPostSimple(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        Set<Video> videos = postRepository.findById(idPost).get().getVideos();
        List<Map<String, Object>> list = new ArrayList<>();
        videos.forEach(video -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idVideo", video.getIdVideo());
            map.put("linkVideo", video.getLinkVideo());
            list.add(map);
        });

//        Sắp xếp theo id tăng dần
        list.sort((l1, l2) -> ((Long) l1.get("idVideo")).compareTo((Long) l2.get("idVideo")));
        return list;
    }

//    Lấy ra thông tin chi tiết của tất cả ảnh trong 1 bài viết
    public static Set<Image> getAllImageByIdPost(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        Set<Image> images = postRepository.findById(idPost).get().getImages();
        return images;
    }


//    Lấy ra thông tin chi tiết của tất cả video trong 1 bài viết
    public static Set<Video> getAllVideoByIdPost(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        Set<Video> videos = postRepository.findById(idPost).get().getVideos();
        return videos;
    }

    public static Long countCommentByIdPost(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        Set<Comment> comments = post.get().getComments();
        Long countComment = Long.valueOf(comments.size());

        for(Comment comment : comments) {
            countComment += comment.getCommentChilds().size();
        }

        return countComment;
    }

//    Lấy ra thông tin chi tiết của tất cả bình luận trong 1 bài viết
    public static Set<Comment> getAllCommentByIdPost(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        Set<Comment> comments = post.get().getComments();
        return comments;
    }

    @Override
    public Post createPostInGroup(Long idGroup, Long idUser, PostDTO postDTO, MultipartFile[] fileImages, MultipartFile[] fileVideos) {
        Optional<Group> group = groupRepository.findById(idGroup);
        groupService.checkGroupExists(group);
        Optional<User> user = userRepository.findById(idUser);
        UserServiceImpl.checkUserExists(user);

        Set<User> users = group.get().getUsers();
        for (User i : users) {
            if(i.getIdUser() == user.get().getIdUser()) {
                Post post = modelMapper.map(postDTO, Post.class);
                post.setUser(user.get());
                post.setGroup(group.get());

                setMediaToPost(post, fileImages, fileVideos);
                postRepository.save(post);
                return post;
            }
        }
        throw new NotFoundException(MessageConstant.USER_NOT_IN_GROUP);
    }


    public static void checkPostExists(Optional<Post> post) {
        if (post.isEmpty()) {
            throw new NotFoundException(MessageConstant.POST_NOT_EXISTS);
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
