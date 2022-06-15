package com.example.strawberry.application.dai;

import com.example.strawberry.domain.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IUserGroupRepository extends JpaRepository<UserGroup, Long> {

    @Query("select u from user_group u where u.user.idUser = ?1 and u.group.idGroup = ?2")
    UserGroup findByUserIdUserAndGroupIdGroup(Long idUser, Long idGroup);

    @Query("select u from user_group u where u.group.idGroup = ?1")
    Set<UserGroup> findByGroupIdGroup(Long idGroup);
}
