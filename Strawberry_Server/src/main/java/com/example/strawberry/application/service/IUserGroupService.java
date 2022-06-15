package com.example.strawberry.application.service;

import com.example.strawberry.domain.dto.GroupDTO;
import com.example.strawberry.domain.entity.Group;
import com.example.strawberry.domain.entity.User;

import java.util.Set;

public interface IUserGroupService {

    Set<User> getAllUserInGroup(Long idGroup);

    Group createGroup(Long idUser, GroupDTO groupDTO);

    Group addUserToGroup(Long idGroup, Long idUser);

    String deleteGroup(Long idGroup, Long idUser);

}
