package com.example.strawberry.adapter.web.v1.controller;

import com.example.strawberry.adapter.web.base.VsResponseUtil;
import com.example.strawberry.application.service.IGroupService;
import com.example.strawberry.domain.dto.GroupDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
    private final IGroupService groupService;

    public GroupController(IGroupService groupService) {
        this.groupService = groupService;
    }

    @ApiOperation(value = "Lấy ra tất cả các nhóm theo quyền truy cập.")
    @GetMapping("/get-by-access")
    public ResponseEntity<?> getGroupByAccess(@RequestParam("access") int access) {
        return VsResponseUtil.ok(groupService.getGroupByAccess(access));
    }

    @ApiOperation(value = "Lấy ra tất cả user của nhóm.")
    @GetMapping("/{id}/users")
    public ResponseEntity<?> getAllGroupByIdUser(@PathVariable Long id) {
        return VsResponseUtil.ok(groupService.getAllUserInGroup(id));
    }

    @ApiOperation(value = "Tạo nhóm.")
    @PostMapping("/{idUser}/create-group")
    public ResponseEntity<?> createGroup(
            @PathVariable("idUser") Long idUser,
            @RequestBody GroupDTO groupDTO) {
        return VsResponseUtil.ok(groupService.createGroup(idUser, groupDTO));
    }

    @ApiOperation(value = "Thêm thành viên vào nhóm.")
    @GetMapping("/{idGroup}/{idUser}/add-user-to-group")
    public ResponseEntity<?> createGroup(
            @PathVariable("idGroup") Long idGroup,
            @PathVariable("idUser") Long idUser) {
        return VsResponseUtil.ok(groupService.addUserToGroup(idGroup, idUser));
    }

    @ApiOperation(value = "Lấy các bài viết trong nhóm.")
    @GetMapping("/{idGroup}/{idUser}/get-post")
    public ResponseEntity<?> getAllPostInGroup(
            @PathVariable("idGroup") Long idGroup,
            @PathVariable("idUser") Long idUser) {
        return VsResponseUtil.ok(groupService.getAllPostInGroup(idGroup, idUser));
    }
}
