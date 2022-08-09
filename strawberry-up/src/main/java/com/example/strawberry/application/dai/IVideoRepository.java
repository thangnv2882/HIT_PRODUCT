package com.example.strawberry.application.dai;

import com.example.strawberry.domain.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVideoRepository extends JpaRepository<Video, Long> {
}
