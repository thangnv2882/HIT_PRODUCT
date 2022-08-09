package com.example.strawberry.application.dai;

import com.example.strawberry.domain.entity.Image;
import com.example.strawberry.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IImageRepository extends JpaRepository<Image, Long> {
}
