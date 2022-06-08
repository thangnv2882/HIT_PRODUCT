package com.example.strawberry.application.dai;

import com.example.strawberry.domain.entity.Group;
import com.example.strawberry.domain.entity.Post;
import com.example.strawberry.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {
    Set<Post> findAllByAccess(int access);

    @Query("select p from Post p where p.user = ?1 and p.group = ?2")
    Set<Post> findByUserAndGroup(User user, Group group);

    @Query("select p from Post p where p.user = ?1 and p.group = ?2 and p.access = ?3")
    Set<Post> findByUserAndGroupAndAccess(User user, Group group, int access);
}
