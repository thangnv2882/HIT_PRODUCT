package com.example.strawberry.application.service.Impl;

import com.example.strawberry.adapter.web.base.AccessType;
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

import static com.example.strawberry.adapter.web.base.AccessType.*;
import static com.example.strawberry.adapter.web.base.ReactionType.*;

@Service
public class PostServiceImpl implements IPostService {

    private final IPostRepository postRepository;
    private final IUserRepository userRepository;
    private final IGroupRepository groupRepository;
    private final IImageRepository imageRepository;
    private final IVideoRepository videoRepository;
    private final IReactionRepository reactionRepository;
    private final GroupServiceImpl groupService;
    private final ModelMapper modelMapper;
    private final UploadFile uploadFile;
    private final IUserGroupRepository userGroupRepository;
    private Slugify slg = new Slugify();

    public PostServiceImpl(IPostRepository postRepository, IUserRepository userRepository, IGroupRepository groupRepository, IImageRepository imageRepository, IVideoRepository videoRepository, IReactionRepository reactionRepository, GroupServiceImpl groupService, ModelMapper modelMapper, UploadFile uploadFile, IUserGroupRepository userGroupRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.imageRepository = imageRepository;
        this.videoRepository = videoRepository;
        this.reactionRepository = reactionRepository;
        this.groupService = groupService;
        this.modelMapper = modelMapper;
        this.uploadFile = uploadFile;
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public Map<?, ?> getPostById(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        return getDetailPost(post.get());
    }

    @Override
    public Post createPost(Long idUser, PostDTO postDTO) {

        Optional<User> user = userRepository.findById(idUser);
        UserServiceImpl.checkUserExists(user);
        Post post = modelMapper.map(postDTO, Post.class);
        if (PRIVATE.equals(postDTO.getAccess())) {
            post.setAccess(PRIVATE);
        }
        else {
            post.setAccess(PUBLIC);
        }
        post.setUser(user.get());
        setMediaToPost(post, postDTO);
        postRepository.save(post);
        return post;
    }

    @Override
    public Post updatePost(Long idUserFix, Long idPost, PostDTO postDTO) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);

        Optional<User> userFix = userRepository.findById(idUserFix);
        UserServiceImpl.checkUserExists(userFix);
        User userOwns = post.get().getUser();
        if (userOwns.getIdUser() != userFix.get().getIdUser()) {
            throw new ExceptionAll("This post is not yours.");
        }

        modelMapper.map(postDTO, post.get());
        setMediaToPost(post.get(), postDTO);
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
        if (userOwns.getIdUser() != userFix.get().getIdUser()) {
            throw new ExceptionAll("This post is not yours.");
        }
        postRepository.delete(post.get());
        return post.get();
    }

    @Override
    public List<?> getAllPostByAccess(AccessType access) {
        Set<Post> posts = postRepository.findAllByAccess(access);
        return getAllPostNotInGroup(posts);
    }

    public List<?> getAllPostNotInGroup(Set<Post> posts) {
        Set<Post> postEnd = new HashSet<>();
        posts.forEach(i -> {
            if (i.getGroup() == null) {
                postEnd.add(i);
            }
        });
        List<Map<String, Object>> list = new ArrayList<>();
        for (Post post : postEnd) {
            Map<String, Object> map = getDetailPost(post);
            list.add(map);
        }

        list.sort((l1, l2) -> ((Long) l2.get("idPost")).compareTo((Long) l1.get("idPost")));

        return list;
    }

    public Map<String, Object> getDetailPost(Post post) {
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
        return map;
    }

//    Lấy ra thông tin chi tiết của tất cả bình luận trong 1 bài viết
    public List<Comment> getAllCommentByIdPost(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        Set<Comment> comments = post.get().getComments();
        List<Comment> commentList = new ArrayList<>(comments);

        commentList.sort((l1, l2) -> (l2.getIdComment()).compareTo(l1.getIdComment()));

        return commentList;
    }

    //    Hiện thị các lượt bày tỏ cảm xúc của bài post này
    public Map<String, Long> getCountReactionOfPost(Long idPost) {
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
    public List<Map<String, Object>> getAllImageByIdPostSimple(Long idPost) {
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
    public List<Map<String, Object>> getAllVideoByIdPostSimple(Long idPost) {
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
    public Set<Image> getAllImageByIdPost(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        Set<Image> images = postRepository.findById(idPost).get().getImages();
        return images;
    }


    //    Lấy ra thông tin chi tiết của tất cả video trong 1 bài viết
    public Set<Video> getAllVideoByIdPost(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        Set<Video> videos = postRepository.findById(idPost).get().getVideos();
        return videos;
    }

    public Long countCommentByIdPost(Long idPost) {
        Optional<Post> post = postRepository.findById(idPost);
        checkPostExists(post);
        Set<Comment> comments = post.get().getComments();
        Long countComment = Long.valueOf(comments.size());

        for (Comment comment : comments) {
            countComment += comment.getCommentChilds().size();
        }

        return countComment;
    }



    @Override
    public Post createPostInGroup(Long idGroup, Long idUser, PostDTO postDTO) {
        Optional<Group> group = groupRepository.findById(idGroup);
        groupService.checkGroupExists(group);
        Optional<User> user = userRepository.findById(idUser);
        UserServiceImpl.checkUserExists(user);

        UserGroup userGroup = userGroupRepository.findByUserIdUserAndGroupIdGroup(idUser, idGroup);

        // Nếu nhóm riêng tư thì chỉ thành viên có thể đăng bài trong nhóm
        if (group.get().getAccess().equals(AccessType.PRIVATE) && userGroup == null) {
            throw new ExceptionAll(MessageConstant.USER_NOT_IN_GROUP);
        }

        // Nếu nhóm công khai thì tất cả mọi người có thể điều có thể đăng bài trong nhóm
        Post post = modelMapper.map(postDTO, Post.class);
        post.setUser(user.get());
        post.setGroup(group.get());

        setMediaToPost(post, postDTO);
        post.setAccess(PUBLIC);
        postRepository.save(post);
        return post;
    }


//    @Override
//    public Set<Post> findByContentPost(String contentPost) {
//        return postRepository.findByContentPost(contentPost);
//    }


    public static void checkPostExists(Optional<Post> post) {
        if (post.isEmpty()) {
            throw new NotFoundException(MessageConstant.POST_NOT_EXISTS);
        }
    }

    public void setMediaToPost(Post post, PostDTO postDTO) {
        List<String> linkImages = postDTO.getLinkImages();
        if (linkImages != null) {
            Set<Image> images = new HashSet<>();
            for (String link : linkImages) {
                Image image = new Image();
                image.setLinkImage(link);
                image.setPost(post);
                imageRepository.save(image);
                images.add(image);
            }
            post.setImages(images);
        } else {
            post.setImages(new HashSet<>());
        }
        List<String> linkVideos = postDTO.getLinkVideos();
        if (linkVideos != null) {
            Set<Video> videos = new HashSet<>();
            for (String link : linkVideos) {
                Video video = new Video();
                video.setLinkVideo(link);
                video.setPost(post);
                videoRepository.save(video);
                videos.add(video);
            }
            post.setVideos(videos);
        } else {
            post.setImages(new HashSet<>());
        }
    }
//
//    public void setMediaToPost(Post post, MultipartFile[] fileImages, MultipartFile[] fileVideos) {
//        if (fileImages != null) {
//            Set<Image> images = new HashSet<>();
//
//            for (MultipartFile file : fileImages) {
//                Image image = new Image();
//                image.setLinkImage(uploadFile.getUrlFromFile(file));
//                image.setPost(post);
//                imageRepository.save(image);
//                images.add(image);
//            }
//            post.setImages(images);
//
//        } else {
//            post.setImages(new HashSet<>());
//        }
//        if (fileVideos != null) {
//            Set<Video> videos = new HashSet<>();
//            for (MultipartFile file : fileVideos) {
//                Video video = new Video();
//                try {
//                    video.setLinkVideo(uploadFile.getUrlFromLargeFile(file));
//                    video.setPost(post);
//                    videoRepository.save(video);
//                    videos.add(video);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            post.setVideos(videos);
//        } else {
//            post.setVideos(new HashSet<>());
//        }
//    }
}
