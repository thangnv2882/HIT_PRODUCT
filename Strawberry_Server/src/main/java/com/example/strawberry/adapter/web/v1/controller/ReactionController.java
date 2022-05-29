package com.example.strawberry.adapter.web.v1.controller;

import com.example.strawberry.adapter.web.base.VsResponseUtil;
import com.example.strawberry.application.service.IReactionService;
import com.example.strawberry.domain.dto.ReactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/reactions")
public class ReactionController {

    private final IReactionService reactionService;

    public ReactionController(IReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @PostMapping("/create-reaction")
    public ResponseEntity<?> createReaction(
            @ModelAttribute ReactionDTO reactionDTO,
            @RequestParam(value = "fileImage", required = false) MultipartFile fileImage
            ) {
        return VsResponseUtil.ok(reactionService.createReaction(reactionDTO, fileImage));
    }
}
