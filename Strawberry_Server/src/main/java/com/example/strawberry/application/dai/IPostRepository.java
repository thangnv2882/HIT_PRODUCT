package com.example.strawberry.application.dai;

import com.example.strawberry.adapter.web.base.AccessType;
import com.example.strawberry.domain.entity.Group;
import com.example.strawberry.domain.entity.Post;
import com.example.strawberry.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long>{

    @Query("select p from Post p where p.access = ?1")
    Set<Post> findAllByAccess(AccessType access);

    @Query("select p from Post p where p.user = ?1 and p.group = ?2")
    Set<Post> findPostByUserAndGroup(User user, Group group);

    @Query("select p from Post p where p.user = ?1 and p.group = ?2 and p.access = ?3")
    Set<Post> findPostByUserAndGroupAndAccess(User user, Group group, AccessType access);

    @Query("select p from Post p where p.user.idUser = ?1 and p.access = ?2 and p.group = ?3")
    Set<Post> findPostByUserIdUserAndAccessAndGroup(Long idUser, int access, Group group);

//    @Query("select p from Post p where p.contentPost = ?1")
//    Set<Post> findByContentPost(String contentPost);

}
