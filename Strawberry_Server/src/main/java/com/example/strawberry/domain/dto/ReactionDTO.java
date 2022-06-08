package com.example.strawberry.domain.dto;

import com.example.strawberry.adapter.web.base.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionDTO {
    private ReactionType reactionType;
    private Long idPost;
    private Long idUserReact;
}
