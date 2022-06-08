package com.example.strawberry.application.service.Impl;

import com.example.strawberry.application.constants.MessageConstant;
import com.example.strawberry.application.dai.IGroupRepository;
import com.example.strawberry.application.dai.IUserRepository;
import com.example.strawberry.application.service.IGroupService;
import com.example.strawberry.config.exception.NotFoundException;
import com.example.strawberry.domain.dto.GroupDTO;
import com.example.strawberry.domain.entity.Group;
import com.example.strawberry.domain.entity.Post;
import com.example.strawberry.domain.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GroupServiceImpl implements IGroupService {

    private final IGroupRepository groupRepository;
    private final IUserRepository userRepository;
    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    public GroupServiceImpl(IGroupRepository groupRepository, IUserRepository userRepository, UserServiceImpl userService, ModelMapper modelMapper) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Group> getAllGroup() {
        return groupRepository.findAll();
    }

    @Override
    public Set<Group> getGroupByAccess(int access) {
        Set<Group> groups = groupRepository.findGroupByAccessIs(access);
        return groups;
    }

    @Override
    public Set<User> getAllUserInGroup(Long idGroup) {
        Optional<Group> group = groupRepository.findById(idGroup);
        checkGroupExists(group);
        Set<User> users = group.get().getUsers();
        return users;
    }

    @Override
    public Group createGroup(Long idUser, GroupDTO groupDTO) {
        Optional<User> user = userRepository.findById(idUser);
        userService.checkUserExists(user);

        Group group = modelMapper.map(groupDTO, Group.class);

        Set<User> users = new HashSet<>();
        Set<Group> groups = user.get().getGroups();

        users.add(user.get());
        groups.add(group);

        user.get().setGroups(groups);
        group.setUsers(users);

        groupRepository.save(group);
        userRepository.save(user.get());

        return group;
    }

    @Override
    public Group addUserToGroup(Long idGroup, Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        userService.checkUserExists(user);

        Optional<Group> group = groupRepository.findById(idGroup);
        checkGroupExists(group);

        // Lấy ra list user trong group hiện tại
        Set<User> users = group.get().getUsers();
        // Thêm user vào list đó
        users.add(user.get());

        // set list user vào group
        group.get().setUsers(users);


        // Lấy ra list group của user
        Set<Group> groups = user.get().getGroups();
        // Thêm group vào list đó
        groups.add(group.get());

        // set list group vào user
        user.get().setGroups(groups);

        groupRepository.save(group.get());
        userRepository.save(user.get());

        return group.get();
    }

    @Override
    public Set<Post> getAllPostInGroup(Long idGroup, Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        userService.checkUserExists(user);
        Optional<Group> group = groupRepository.findById(idGroup);
        checkGroupExists(group);

        Set<Post> posts = group.get().getPosts();
        Set<User> users = group.get().getUsers();

        if (group.get().getAccess() == 0) {
            for (User i : users) {
                if (i.getId() == user.get().getId()) {
                    return posts;
                }
            }
            return new HashSet<>();
        }
        return posts;
    }

    public void checkGroupExists(Optional<Group> group) {
        if (group.isEmpty()) {
            throw new NotFoundException(MessageConstant.GROUP_NOT_EXISTS);
        }
    }
}
