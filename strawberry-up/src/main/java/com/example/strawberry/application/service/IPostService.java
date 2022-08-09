package com.example.strawberry.application.service;

import com.example.strawberry.adapter.web.base.AccessType;
import com.example.strawberry.domain.dto.PostDTO;
import com.example.strawberry.domain.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IPostService {
    Map<?, ?> getPostById(Long idPost);

    List<?> getAllPostByAccess(AccessType access);

    Post createPost(Long idUser, PostDTO postDTO);

    Post updatePost(Long idUserFix, Long idPost, PostDTO postDTO);

    Post deletePostById(Long idUserFix, Long idPost);

    Set<Image> getAllImageByIdPost(Long idPost);

    Set<Video> getAllVideoByIdPost(Long idPost);

    List<Comment> getAllCommentByIdPost(Long idPost);

    Post createPostInGroup(Long idGroup, Long idUser, PostDTO postDTO);

}
