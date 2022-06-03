package com.example.strawberry.application.dai;

import com.example.strawberry.domain.entity.Post;
import com.example.strawberry.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByEmailOrPhoneNumber(String email, String phoneNumber);

}
