package com.example.strawberry.adapter.web.v1.controller;

import com.example.strawberry.adapter.web.base.AccessType;
import com.example.strawberry.adapter.web.base.RestApiV1;
import com.example.strawberry.adapter.web.base.VsResponseUtil;
import com.example.strawberry.application.constants.UrlConstant;
import com.example.strawberry.application.service.IPostService;
import com.example.strawberry.domain.dto.PostDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestApiV1
public class PostController {

    private final IPostService postService;

    public PostController(IPostService postService) {
        this.postService = postService;
    }

    @ApiOperation(value = "Lấy ra bài đăng theo id")
    @GetMapping(UrlConstant.Post.DATA_POST_GET_POST)
    public ResponseEntity<?> getPostById(
            @PathVariable("idPost") Long idPost) {
        return VsResponseUtil.ok(postService.getPostById(idPost));
    }

    @ApiOperation(value = "Lấy ra tất cả bài đăng công khai.")
    @GetMapping(UrlConstant.Post.DATA_POST)
    public ResponseEntity<?> getAllPost() {
        return VsResponseUtil.ok(postService.getAllPostByAccess(AccessType.PUBLIC));
    }

    @ApiOperation(value = "Đăng bài trên tường")
    @PostMapping(UrlConstant.Post.DATA_POST_CREATE_POST)
    public ResponseEntity<?> createPost(
            @PathVariable("idUser") Long id,
            @RequestBody PostDTO postDTO) {
        return VsResponseUtil.ok(postService.createPost(id, postDTO));
    }

    @ApiOperation(value = "Đăng bài trong nhóm")
    @PostMapping(UrlConstant.Post.DATA_POST_CREATE_IN_GROUP)
    public ResponseEntity<?> createPost(
            @PathVariable("idGroup") Long idGroup,
            @PathVariable("idUser") Long idUser,
            @RequestBody PostDTO postDTO) {
        return VsResponseUtil.ok(postService.createPostInGroup(idGroup, idUser, postDTO));
    }

    @ApiOperation(value = "Chỉnh sửa bài đăng")
    @PatchMapping(UrlConstant.Post.DATA_POST_UPDATE_POST)
    public ResponseEntity<?> updatePost(
            @PathVariable("idUserFix") Long idUserFix,
            @PathVariable("idPost") Long idPost,
            @RequestBody PostDTO postDTO,
            @RequestParam(name = "fileImages", required = false) MultipartFile[] fileImages,
            @RequestParam(name = "fileVideos", required = false) MultipartFile[] fileVideos) {
        return VsResponseUtil.ok(postService.updatePost(idUserFix, idPost, postDTO));
    }

    @ApiOperation(value = "Xoá bài đăng")
    @DeleteMapping(UrlConstant.Post.DATA_POST_DELETE_POST)
    public ResponseEntity<?> deletePost(
            @PathVariable("idUserFix") Long idUserFix,
            @PathVariable("idPost") Long idPost) {
        return VsResponseUtil.ok(postService.deletePostById(idUserFix, idPost));
    }

    @ApiOperation(value = "Lấy ra tất cả ảnh của bài đăng theo id.")
    @GetMapping(UrlConstant.Post.DATA_POST_GET_IMAGES)
    public ResponseEntity<?> getAllImageById(
            @PathVariable("idPost") Long idPost) {
        return VsResponseUtil.ok(postService.getAllImageByIdPost(idPost));
    }

    @ApiOperation(value = "Lấy ra tất cả video của bài đăng theo id.")
    @GetMapping(UrlConstant.Post.DATA_POST_GET_VIDEOS)
    public ResponseEntity<?> getAllVideoById(
            @PathVariable("idPost") Long idPost) {
        return VsResponseUtil.ok(postService.getAllVideoByIdPost(idPost));
    }

    @ApiOperation(value = "Lấy ra tất cả bình luận của bài đăng.")
    @GetMapping(UrlConstant.Post.DATA_POST_GET_COMMENTS)
    public ResponseEntity<?> getAllCommentByIdPost(
            @PathVariable("idPost") Long idPost
    ) {
        return VsResponseUtil.ok(postService.getAllCommentByIdPost(idPost));
    }

//    @ApiOperation(value = "Tìm kiếm bài viết.")
//    @GetMapping(UrlConstant.Post.DATA_POST_FIND_BY_CONTENT)
//    public ResponseEntity<?> findByContentPost(
//            @RequestParam("contentFind") String contentFind
//    ) {
//        return VsResponseUtil.ok(postService.findByContentPost(contentFind));
//    }
}
