package com.example.strawberry.application.service;

import com.example.strawberry.domain.dto.PostDTO;
import com.example.strawberry.domain.dto.ReactionDTO;
import com.example.strawberry.domain.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface IPostService {
    Set<Post> getAllPostPublic(int i);
    Post createPost(Long idUser, PostDTO postDTO, MultipartFile[] fileImages, MultipartFile[] fileVideos);
    Post updatePost(Long idUserFix, Long idPost, PostDTO postDTO, MultipartFile[] fileImages, MultipartFile[] fileVideos);
    Post deletePostById(Long idUserFix, Long idPost);

    Set<Image> getAllImageById(Long id);
    Set<Video> getAllVideoById(Long id);

//    Post setReactByIdPost(ReactionDTO reactionDTO);
//    Set<Reaction> getAllReactionByIdPost(Long idPost);

    Set<Comment> getAllCommentByIdPost(Long idPost);

    Post createPostInGroup(Long idGroup, Long idUser, PostDTO postDTO, MultipartFile[] fileImages, MultipartFile[] fileVideos);

}
