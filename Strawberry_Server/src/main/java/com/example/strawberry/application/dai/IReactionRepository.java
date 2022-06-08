package com.example.strawberry.application.dai;

import com.example.strawberry.adapter.web.base.ReactionType;
import com.example.strawberry.domain.entity.Post;
import com.example.strawberry.domain.entity.Reaction;
import com.example.strawberry.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IReactionRepository extends JpaRepository<Reaction, Long> {

    @Query("select r from Reaction r where r.post.id = ?1 and r.user.id = ?2")
    Reaction findByPostIdAndUserId(Long idPost, Long idUser);

    @Query("select count(r) from Reaction r where r.post.id = ?1")
    Long countByPostId(Long idPost);

    Long countByPostIdAndAndReactionType(Long idPost, ReactionType reactionType);

}
