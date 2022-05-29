package com.example.strawberry.application.service;

import com.example.strawberry.domain.dto.ReactionDTO;
import com.example.strawberry.domain.entity.Reaction;
import org.springframework.web.multipart.MultipartFile;

public interface IReactionService {
    Reaction createReaction(ReactionDTO reactionDTO, MultipartFile fileImage);
}
