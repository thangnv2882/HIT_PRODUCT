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

    @Query("select r from Reaction r where r.post.idPost = ?1 and r.user.idUser = ?2")
    Reaction findByPostIdPostAndUserIdUser(Long idPost, Long idUser);

    @Query("select count(r) from Reaction r where r.post.idPost = ?1")
    Long countByPostIdPost(Long idPost);

    @Query("select count(r) from Reaction r where r.post.idPost = ?1 and r.reactionType = ?2")
    Long countByPostIdPostAndAndReactionType(Long idPost, ReactionType reactionType);

}
