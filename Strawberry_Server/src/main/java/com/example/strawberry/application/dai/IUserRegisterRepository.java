package com.example.strawberry.application.dai;

import com.example.strawberry.domain.entity.User;
import com.example.strawberry.domain.entity.UserRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRegisterRepository extends JpaRepository<UserRegister, Long> {
    List<UserRegister> findAllByStatusIs(Boolean status);
    UserRegister findByEmailOrPhoneNumber(String email, String phoneNumber);
}
