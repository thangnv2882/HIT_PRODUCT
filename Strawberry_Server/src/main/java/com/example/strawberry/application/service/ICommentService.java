package com.example.strawberry.application.service;

import com.example.strawberry.domain.dto.CommentDTO;
import com.example.strawberry.domain.entity.Comment;

import java.util.Set;

public interface ICommentService {
    Comment createCommentForPost(Long idUser, Long idPost, CommentDTO commentDTO);
    Comment updateCommentForPost(Long idUser, Long idComment, CommentDTO commentDTO);
    Comment deleteCommentForPost(Long idUser, Long idComment);

    Comment createCommentForComment(Long idUser, Long idCommentParent, CommentDTO commentDTO);
    Set<Comment> getAllCommentChild(Long idCommentParent);


}
