package com.example.strawberry.application.dai;

import com.example.strawberry.adapter.web.base.AccessType;
import com.example.strawberry.domain.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IGroupRepository extends JpaRepository<Group, Long> {

    Set<Group> findByAccess(AccessType access);

}
