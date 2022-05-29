package com.example.strawberry.application.dai;

import com.example.strawberry.domain.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReactionRepository extends JpaRepository<Reaction, Long> {
}
