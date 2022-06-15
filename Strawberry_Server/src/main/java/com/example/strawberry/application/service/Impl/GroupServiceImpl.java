package com.example.strawberry.application.service.Impl;

import com.example.strawberry.application.constants.MessageConstant;
import com.example.strawberry.application.dai.IGroupRepository;
import com.example.strawberry.application.dai.IUserGroupRepository;
import com.example.strawberry.application.dai.IUserRepository;
import com.example.strawberry.application.service.IGroupService;
import com.example.strawberry.config.exception.ExceptionAll;
import com.example.strawberry.config.exception.NotFoundException;
import com.example.strawberry.domain.entity.Group;
import com.example.strawberry.domain.entity.Post;
import com.example.strawberry.domain.entity.User;
import com.example.strawberry.domain.entity.UserGroup;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GroupServiceImpl implements IGroupService {

    private final IGroupRepository groupRepository;
    private final IUserRepository userRepository;
    private final IUserGroupRepository userGroupRepository;
    private final UserServiceImpl userService;

    public GroupServiceImpl(IGroupRepository groupRepository, IUserRepository userRepository, IUserGroupRepository userGroupRepository, UserServiceImpl userService) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.userService = userService;
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
    public Set<Post> getAllPostInGroup(Long idGroup, Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        userService.checkUserExists(user);

        Optional<Group> group = groupRepository.findById(idGroup);
        checkGroupExists(group);

        UserGroup userGroup = userGroupRepository.findByUserIdUserAndGroupIdGroup(idUser, idGroup);
        Set<Post> posts = group.get().getPosts();

        // Nếu nhóm riêng tư thì chỉ thành viên có thể xem các bài viết trong nhóm
        if (group.get().getAccess() == 0) {
            if (userGroup != null) {
                return posts;
            }
            throw new ExceptionAll(MessageConstant.USER_NOT_IN_GROUP);
        }

        // Nếu nhóm công khai thì tất cả mọi người có thể xem các bài viết trong nhóm
        return posts;
    }

    public void checkGroupExists(Optional<Group> group) {
        if (group.isEmpty()) {
            throw new NotFoundException(MessageConstant.GROUP_NOT_EXISTS);
        }
    }
}
