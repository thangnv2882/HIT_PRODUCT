package com.example.strawberry.adapter.web.v1.controller;

import com.example.strawberry.adapter.web.base.RestApiV1;
import com.example.strawberry.adapter.web.base.VsResponseUtil;
import com.example.strawberry.application.constants.UrlConstant;
import com.example.strawberry.application.service.IGroupService;
import com.example.strawberry.domain.dto.GroupDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
public class GroupController {
    private final IGroupService groupService;

    public GroupController(IGroupService groupService) {
        this.groupService = groupService;
    }

    @ApiOperation(value = "Lấy ra tất cả các nhóm.")
    @GetMapping(UrlConstant.Group.DATA_GROUP)
    public ResponseEntity<?> getAllGroup() {
        return VsResponseUtil.ok(groupService.getAllGroup());
    }

    @ApiOperation(value = "Lấy ra tất cả các nhóm theo quyền truy cập.")
    @GetMapping(UrlConstant.Group.DATA_GROUP_BY_ACCESS)
    public ResponseEntity<?> getGroupByAccess(
            @RequestParam("access") int access) {
        return VsResponseUtil.ok(groupService.getGroupByAccess(access));
    }


    @ApiOperation(value = "Lấy ra tất cả user của nhóm.")
    @GetMapping(UrlConstant.Group.DATA_GROUP_ALL_USER)
    public ResponseEntity<?> getAllGroupByIdUser(@PathVariable("idGroup") Long idGroup) {
        return VsResponseUtil.ok(groupService.getAllUserInGroup(idGroup));
    }

    @ApiOperation(value = "Tạo nhóm.")
    @PostMapping(UrlConstant.Group.DATA_GROUP_CREATE_GROUP)
    public ResponseEntity<?> createGroup(
            @PathVariable("idUser") Long idUser,
            @RequestBody GroupDTO groupDTO) {
        return VsResponseUtil.ok(groupService.createGroup(idUser, groupDTO));
    }

    @ApiOperation(value = "Thêm thành viên vào nhóm.")
    @GetMapping(UrlConstant.Group.DATA_GROUP_ADD_MEMBER)
    public ResponseEntity<?> createGroup(
            @PathVariable("idGroup") Long idGroup,
            @PathVariable("idUser") Long idUser) {
        return VsResponseUtil.ok(groupService.addUserToGroup(idGroup, idUser));
    }

    @ApiOperation(value = "Lấy các bài viết trong nhóm.")
    @GetMapping(UrlConstant.Group.DATA_GROUP_GET_POSTS)
    public ResponseEntity<?> getAllPostInGroup(
            @PathVariable("idGroup") Long idGroup,
            @PathVariable("idUser") Long idUser) {
        return VsResponseUtil.ok(groupService.getAllPostInGroup(idGroup, idUser));
    }

}
