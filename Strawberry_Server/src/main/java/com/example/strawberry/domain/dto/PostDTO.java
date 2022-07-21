package com.example.strawberry.domain.dto;

import com.example.strawberry.adapter.web.base.AccessType;
import com.example.strawberry.domain.entity.Image;
import com.example.strawberry.domain.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private String contentPost;
    private AccessType access;
    private List<String> linkImages;
    private List<String> linkVideos;

}
