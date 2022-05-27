package com.example.strawberry.application.dai;

import com.example.strawberry.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {
//    List<Post> findAllByIdUser
}
