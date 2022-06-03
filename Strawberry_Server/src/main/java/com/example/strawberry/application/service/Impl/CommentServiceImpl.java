package com.example.strawberry.application.service.Impl;

import com.example.strawberry.application.constants.MessageConstant;
import com.example.strawberry.application.dai.ICommentRepository;
import com.example.strawberry.application.dai.IImageRepository;
import com.example.strawberry.application.dai.IPostRepository;
import com.example.strawberry.application.service.ICommentService;
import com.example.strawberry.config.exception.NotFoundException;
import com.example.strawberry.domain.dto.CommentDTO;
import com.example.strawberry.domain.entity.Comment;
import com.example.strawberry.domain.entity.Post;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CommentServiceImpl implements ICommentService {

    private final ICommentRepository commentRepository;
    private final PostServiceImpl postService;
    private final IPostRepository postRepository;
    private final ModelMapper modelMapper;

    public CommentServiceImpl(ICommentRepository commentRepository, PostServiceImpl postService, IPostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Comment createCommentForPost(Long idPost, CommentDTO commentDTO) {
        Optional<Post> post = postRepository.findById(idPost);
        postService.checkPostExists(post);
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setPost(post.get());
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public Comment updateCommentForPost(Long idComment, CommentDTO commentDTO) {
        Optional<Comment> comment = commentRepository.findById(idComment);
        checkCommentExists(comment);
        modelMapper.map(commentDTO, comment.get());
        commentRepository.save(comment.get());
        return comment.get();
    }

    @Override
    public Comment deleteCommentForPost(Long idComment) {
        Optional<Comment> comment = commentRepository.findById(idComment);
        checkCommentExists(comment);
        commentRepository.delete(comment.get());
        return comment.get();
    }

    @Override
    public Comment createCommentForComment(Long idCommentParent, CommentDTO commentDTO) {
        Optional<Comment> commentParent = commentRepository.findById(idCommentParent);
        checkCommentExists(commentParent);
        Comment commentChild = modelMapper.map(commentDTO, Comment.class);
        commentChild.setCommentParent(commentParent.get());
        commentRepository.save(commentChild);
        return commentChild;
    }

    @Override
    public Set<Comment> getAllCommentChild(Long idCommentParent) {
        Optional<Comment> commentParent = commentRepository.findById(idCommentParent);
        checkCommentExists(commentParent);
        Set<Comment> comments = commentParent.get().getCommentChilds();
        return comments;
    }

    public void checkCommentExists(Optional<Comment> comment) {
        if (comment.isEmpty()) {
            throw new NotFoundException(MessageConstant.COMMENT_NOT_EXISTS);
        }
    }


}
