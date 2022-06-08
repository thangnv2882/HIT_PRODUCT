package com.example.strawberry.application.service.Impl;

import com.example.strawberry.adapter.web.base.ReactionType;
import com.example.strawberry.application.dai.IPostRepository;
import com.example.strawberry.application.dai.IReactionRepository;
import com.example.strawberry.application.dai.IUserRepository;
import com.example.strawberry.application.service.IReactionService;
import com.example.strawberry.config.exception.ExceptionAll;
import com.example.strawberry.domain.dto.ReactionDTO;
import com.example.strawberry.domain.entity.Post;
import com.example.strawberry.domain.entity.Reaction;
import com.example.strawberry.domain.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import static com.example.strawberry.adapter.web.base.ReactionType.DISLIKE;
import static com.example.strawberry.adapter.web.base.ReactionType.LIKE;

@Service
public class ReactionServiceImpl implements IReactionService {

    private final IReactionRepository reactionRepository;
    private final IPostRepository postRepository;
    private final IUserRepository userRepository;
    private final PostServiceImpl postService;
    private final UserServiceImpl userService;


    public ReactionServiceImpl(IReactionRepository reactionRepository, IPostRepository postRepository, IUserRepository userRepository, PostServiceImpl postService, UserServiceImpl userService) {
        this.reactionRepository = reactionRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @Override
    public Reaction setReactionForPost(ReactionDTO reactionDTO) {
        Optional<Post> post = postRepository.findById(reactionDTO.getIdPost());
        postService.checkPostExists(post);

        Optional<User> userReact = userRepository.findById(reactionDTO.getIdUserReact());
        userService.checkUserExists(userReact);

        Reaction reaction = reactionRepository.findByPostIdAndUserId(reactionDTO.getIdPost(), reactionDTO.getIdUserReact());
        if (reaction != null
            && reaction.getReactionType().equals(reactionDTO.getReactionType())) {
            reactionRepository.delete(reaction);
            return reaction;
        }

        reaction = new Reaction();
        reaction.setPost(post.get());
        reaction.setUser(userReact.get());

        if (LIKE.equals(reactionDTO.getReactionType())) {
            reaction.setReactionType(LIKE);
        } else if (DISLIKE.equals(reactionDTO.getReactionType())) {
            reaction.setReactionType(DISLIKE);
        }

        reactionRepository.save(reaction);
        return reaction;
    }

    @Override
    public Long getCountReactionOfPost(Long idPost) {
        return reactionRepository.countByPostId(idPost);
    }

    @Override
    public Long getCountReactionTypeOfPost(Long idPost, ReactionType reactionType) {
        return reactionRepository.countByPostIdAndAndReactionType(idPost, reactionType);
    }
}
