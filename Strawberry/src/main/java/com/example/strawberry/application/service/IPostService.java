package com.example.strawberry.application.service;

import com.example.strawberry.domain.dto.PostDTO;
import com.example.strawberry.domain.entity.Image;
import com.example.strawberry.domain.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface IPostService {
    Post createPost(Long idUser, PostDTO postDTO, MultipartFile[] fileImage);
    Post updatePost(Long id, PostDTO postDTO);
    Post deletePostById(Long id);


    Set<Image> getAllImageById(Long id);

}
