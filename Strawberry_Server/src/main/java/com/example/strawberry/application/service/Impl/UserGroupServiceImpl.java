package com.example.strawberry.application.service.Impl;

import com.example.strawberry.application.constants.MessageConstant;
import com.example.strawberry.application.dai.IGroupRepository;
import com.example.strawberry.application.dai.IUserRepository;
import com.example.strawberry.application.dai.IUserGroupRepository;
import com.example.strawberry.application.service.IUserGroupService;
import com.example.strawberry.config.exception.ExceptionAll;
import com.example.strawberry.config.exception.NotFoundException;
import com.example.strawberry.domain.dto.GroupDTO;
import com.example.strawberry.domain.entity.Group;
import com.example.strawberry.domain.entity.User;
import com.example.strawberry.domain.entity.UserGroup;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.example.strawberry.adapter.web.base.AccessType.*;

@Service
public class UserGroupServiceImpl implements IUserGroupService {
    private final IUserGroupRepository userGroupRepository;
    private final IGroupRepository groupRepository;
    private final IUserRepository userRepository;
    private final UserServiceImpl userService;
    private final GroupServiceImpl groupService;
    private final ModelMapper modelMapper;



    public UserGroupServiceImpl(IUserGroupRepository userGroupRepository, IGroupRepository groupRepository, IUserRepository userRepository, UserServiceImpl userService, GroupServiceImpl groupService, ModelMapper modelMapper) {
        this.userGroupRepository = userGroupRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.groupService = groupService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<User> getAllUserInGroup(Long idGroup) {
        Optional<Group> group = groupRepository.findById(idGroup);
        groupService.checkGroupExists(group);

        Set<UserGroup> userGroups = userGroupRepository.findByGroupIdGroup(idGroup);
        Set<User> users = new HashSet<>();
        userGroups.forEach(item -> {
            users.add(item.getUser());
        });

        return users;
    }

    @Override
    public Group createGroup(Long idUser, GroupDTO groupDTO) {
        Optional<User> user = userRepository.findById(idUser);
        userService.checkUserExists(user);

        Group group = modelMapper.map(groupDTO, Group.class);
        if (PRIVATE.equals(groupDTO.getAccess())) {
            group.setAccess(PRIVATE);
        }
        else {
            group.setAccess(PUBLIC);
        }
        groupRepository.save(group);

        UserGroup userGroup = new UserGroup();
        userGroup.setGroup(group);
        userGroup.setUser(user.get());
        userGroup.setIdUserCreated(user.get().getIdUser());

        userGroupRepository.save(userGroup);

        return group;
    }

    @Override
    public Group addUserToGroup(Long idGroup, Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        userService.checkUserExists(user);

        Optional<Group> group = groupRepository.findById(idGroup);
        groupService.checkGroupExists(group);

        UserGroup userGroup = userGroupRepository.findByUserIdUserAndGroupIdGroup(idUser, idGroup);

        if(userGroup != null) {
            throw new ExceptionAll(MessageConstant.USER_IS_MEMBER_GROUP);
        }

        userGroup = new UserGroup();
        userGroup.setUser(user.get());
        userGroup.setGroup(group.get());
        userGroupRepository.save(userGroup);

        return group.get();
    }

    @Override
    public Group deleteUserFromGroup(Long idGroup, Long idUserDelete, Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        userService.checkUserExists(user);

        Optional<Group> group = groupRepository.findById(idGroup);
        groupService.checkGroupExists(group);

        UserGroup userGroupDelete = userGroupRepository.findByUserIdUserAndGroupIdGroup(idUser, idGroup);
        UserGroup userGroupAdmin = userGroupRepository.findByUserIdUserAndGroupIdGroup(idUserDelete, idGroup);

//        Quản trị viên có quyền xoá thành viên
        if(userGroupDelete == null || userGroupAdmin == null) {
            throw new ExceptionAll(MessageConstant.USER_IS_NOT_MEMBER_GROUP);
        }
        else if(userGroupDelete.getIdUserCreated() == null && userGroupAdmin.getIdUserCreated() != null) {
            userGroupRepository.delete(userGroupDelete);
        }
        else {
            throw new ExceptionAll(MessageConstant.USER_NOT_IS_ADMIN_GROUP);
        }

        return group.get();
    }

    @Override
    public String deleteGroup(Long idGroup, Long idUser) {
        UserGroup userGroup = userGroupRepository.findByUserIdUserAndGroupIdGroup(idUser, idGroup);
        checkUserGroupExists(userGroup);

        if(userGroup.getIdUserCreated() != idUser) {
            throw new ExceptionAll(MessageConstant.USER_NOT_IS_ADMIN_GROUP);
        }

        Optional<Group> group = groupRepository.findById(idGroup);
        groupService.checkGroupExists(group);

        userGroupRepository.delete(userGroup);
        groupRepository.delete(group.get());

        return MessageConstant.DELETE_GROUP_SUSCESS;
    }

    public void checkUserGroupExists(UserGroup userGroup) {
        if (userGroup == null) {
            throw new NotFoundException(MessageConstant.NOT_FOUND);
        }
    }

}
