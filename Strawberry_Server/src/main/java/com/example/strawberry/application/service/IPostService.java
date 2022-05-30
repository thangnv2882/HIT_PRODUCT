package com.example.strawberry.application.service;

import com.example.strawberry.domain.dto.PostDTO;
import com.example.strawberry.domain.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface IPostService {
    Post createPost(Long idUser, PostDTO postDTO, MultipartFile[] fileImages, MultipartFile[] fileVideos);
    Post updatePost(Long id, PostDTO postDTO, MultipartFile[] fileImages, MultipartFile[] fileVideos);
    Post deletePostById(Long id);

    Set<Image> getAllImageById(Long id);
    Set<Video> getAllVideoById(Long id);

    Reaction setReactByIdPost(Long idPost, Long idReation);
    Set<Reaction> getAllReactionByIdPost(Long idPost);

    Set<Comment> getAllCommentByIdPost(Long idPost);

}