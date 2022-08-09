package com.example.strawberry.application.service.Impl;

import com.example.strawberry.application.constants.MessageConstant;
import com.example.strawberry.application.dai.ICommentRepository;
import com.example.strawberry.application.dai.IPostRepository;
import com.example.strawberry.application.dai.IUserRepository;
import com.example.strawberry.application.service.ICommentService;
import com.example.strawberry.config.exception.ExceptionAll;
import com.example.strawberry.config.exception.NotFoundException;
import com.example.strawberry.domain.dto.CommentDTO;
import com.example.strawberry.domain.entity.Comment;
import com.example.strawberry.domain.entity.Post;
import com.example.strawberry.domain.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CommentServiceImpl implements ICommentService {

    private final ICommentRepository commentRepository;
    private final PostServiceImpl postService;
    private final UserServiceImpl userService;
    private final IUserRepository userRepository;
    private final IPostRepository postRepository;
    private final ModelMapper modelMapper;

    public CommentServiceImpl(ICommentRepository commentRepository, PostServiceImpl postService, UserServiceImpl userService, IUserRepository userRepository, IPostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Comment createCommentForPost(Long idUser, Long idPost, CommentDTO commentDTO) {
        Optional<User> user = userRepository.findById(idUser);
        userService.checkUserExists(user);
        Optional<Post> post = postRepository.findById(idPost);
        postService.checkPostExists(post);
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setPost(post.get());
        comment.setUser(user.get());
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public Comment createCommentForComment(Long idUser, Long idCommentParent, CommentDTO commentDTO) {
        Optional<User> user = userRepository.findById(idUser);
        userService.checkUserExists(user);
        Optional<Comment> commentParent = commentRepository.findById(idCommentParent);
        checkCommentExists(commentParent);
        Comment commentChild = modelMapper.map(commentDTO, Comment.class);
        commentChild.setCommentParent(commentParent.get());
        commentChild.setUser(user.get());
        commentRepository.save(commentChild);
        return commentChild;
    }


    @Override
    public Comment updateCommentForPost(Long idUserFix, Long idComment, CommentDTO commentDTO) {
        Optional<Comment> comment = commentRepository.findById(idComment);
        checkCommentExists(comment);

//        User là chủ sở hữu comment mới có quyền chỉnh sửa
        Optional<User> userFix = userRepository.findById(idUserFix);
        userService.checkUserExists(userFix);
        User userOwns = comment.get().getUser();
        if(userOwns.getIdUser() != userFix.get().getIdUser()) {
            throw new ExceptionAll("This comment is not yours.");
        }
        modelMapper.map(commentDTO, comment.get());
        commentRepository.save(comment.get());
        return comment.get();
    }

    @Override
    public Comment deleteCommentForPost(Long idUserFix, Long idComment) {
        Optional<Comment> comment = commentRepository.findById(idComment);
        checkCommentExists(comment);

        // User là chủ sở hữu comment mới có quyền xoá
        Optional<User> userFix = userRepository.findById(idUserFix);
        userService.checkUserExists(userFix);
        User userOwns = comment.get().getUser();



        commentRepository.delete(comment.get());
        return comment.get();
    }

//    //    Lấy ra thông tin chi tiết của tất cả bình luận trong 1 bài viết
//    public Set<Comment> getAllCommentByIdPost(Long idPost) {
//        Optional<Post> post = postRepository.findById(idPost);
//        PostServiceImpl.checkPostExists(post);
//        Set<Comment> comments = post.get().getComments();
//
////        Mới nhất xếp trên
//        comments.stream().sorted((l1, l2) -> (l2.getIdComment()).compareTo(l1.getIdComment()));
//        return comments;
//    }

    @Override
    public Set<Comment> getAllCommentChild(Long idCommentParent) {
        Optional<Comment> commentParent = commentRepository.findById(idCommentParent);
        checkCommentExists(commentParent);
        Set<Comment> comments = commentParent.get().getCommentChilds();

//        Bình luận cũ hiển thị ở trên bình luận mới
        comments.stream().sorted((o1, o2) -> (o1.getIdComment()).compareTo(o2.getIdComment()));
        return comments;
    }

    public void checkCommentExists(Optional<Comment> comment) {
        if (comment.isEmpty()) {
            throw new NotFoundException(MessageConstant.COMMENT_NOT_EXISTS);
        }
    }


}
