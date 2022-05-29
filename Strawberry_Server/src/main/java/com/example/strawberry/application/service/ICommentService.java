package com.example.strawberry.application.service;

import com.example.strawberry.domain.dto.CommentDTO;
import com.example.strawberry.domain.entity.Comment;

import java.util.Set;

public interface ICommentService {
    Comment createCommentForPost(Long idPost, CommentDTO commentDTO);
    Comment updateCommentForPost(Long idComment, CommentDTO commentDTO);
    Comment deleteCommentForPost(Long idComment);

    Comment createCommentForComment(Long idCommentParent, CommentDTO commentDTO);
    Set<Comment> getAllCommentChild(Long idCommentParent);


}
