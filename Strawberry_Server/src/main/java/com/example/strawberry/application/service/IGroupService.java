package com.example.strawberry.application.service;

import com.example.strawberry.domain.dto.GroupDTO;
import com.example.strawberry.domain.entity.Group;
import com.example.strawberry.domain.entity.User;

import java.util.Set;

public interface IGroupService {
    Set<Group> getGroupByAccess(int access);

    Set<User> getAllUserInGroup(Long idGroup);
    Group createGroup(Long idUser, GroupDTO groupDTO);
    User addUserToGroup(Long idGroup, Long idUser);
}
