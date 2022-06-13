package com.example.strawberry.application.dai;

import com.example.strawberry.domain.entity.Post;
import com.example.strawberry.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.idUser = ?1")
    User findByIdUser(Long idUser);

    @Query("select u from User u where u.email = ?1")
    User findByEmail(String email);


    User findByEmailOrPhoneNumber(String email, String phoneNumber);

}
