package com.example.strawberry.application.service;

import com.example.strawberry.adapter.web.base.ReactionType;
import com.example.strawberry.domain.dto.ReactionDTO;
import com.example.strawberry.domain.entity.Post;
import com.example.strawberry.domain.entity.Reaction;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface IReactionService {
    Reaction setReactionForPost(ReactionDTO reactionDTO);
    Long getCountReactionOfPost(Long idPost);
    Long getCountReactionTypeOfPost(Long idPost, ReactionType reactionType);
}
