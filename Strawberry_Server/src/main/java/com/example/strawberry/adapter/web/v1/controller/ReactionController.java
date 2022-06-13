package com.example.strawberry.adapter.web.v1.controller;

import com.example.strawberry.adapter.web.base.RestApiV1;
import com.example.strawberry.adapter.web.base.VsResponseUtil;
import com.example.strawberry.application.constants.UrlConstant;
import com.example.strawberry.application.service.IReactionService;
import com.example.strawberry.domain.dto.ReactionDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
public class ReactionController {

    private final IReactionService reactionService;

    public ReactionController(IReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @ApiOperation(value = "Reaction bài post.")
    @PostMapping(UrlConstant.Reaction.DATA_REACTION_SET_REACTION)
    public ResponseEntity<?> setReactByIdPost(
            @RequestBody ReactionDTO reactionDTO
    ) {
        return VsResponseUtil.ok(reactionService.setReactionForPost(reactionDTO));
    }

    @ApiOperation(value = "Lấy ra số lượng reaction của bài post")
    @GetMapping(UrlConstant.Reaction.DATA_REACTION_GET_COUNT)
    public ResponseEntity<?> getCountReactionOfPost(
            @PathVariable("idPost") Long idPost
    ) {
        return VsResponseUtil.ok(reactionService.getCountReactionOfPost(idPost));
    }
}
