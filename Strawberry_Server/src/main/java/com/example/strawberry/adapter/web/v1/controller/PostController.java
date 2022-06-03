package com.example.strawberry.adapter.web.v1.controller;

import com.example.strawberry.adapter.web.base.VsResponseUtil;
import com.example.strawberry.application.service.IPostService;
import com.example.strawberry.domain.dto.PostDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final IPostService postService;

    public PostController(IPostService postService) {
        this.postService = postService;
    }

    @ApiOperation(value = "Lấy ra tất cả bài post công khai.")
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllPost() {
        return VsResponseUtil.ok(postService.getAllPostPublic(2));
    }

    @ApiOperation(value = "Đăng bài trên tường")
    @PostMapping("/{idUser}/create-post")
    public ResponseEntity<?> createPost(
            @PathVariable("idUser") Long id,
            @ModelAttribute PostDTO postDTO,
            @RequestParam(name = "fileImages", required = false) MultipartFile[] fileImages,
            @RequestParam(name = "fileVideos", required = false) MultipartFile[] fileVideos) {
        System.out.println("aaa");
        return VsResponseUtil.ok(postService.createPost(id, postDTO, fileImages, fileVideos));
    }

    @ApiOperation(value = "Đăng bài trong nhóm")
    @PostMapping("/{idGroup}/{idUser}/create-post-in-group")
    public ResponseEntity<?> createPost(
            @PathVariable("idGroup") Long idGroup,
            @PathVariable("idUser") Long idUser,
            @ModelAttribute PostDTO postDTO,
            @RequestParam(name = "fileImages", required = false) MultipartFile[] fileImages,
            @RequestParam(name = "fileVideos", required = false) MultipartFile[] fileVideos) {
        System.out.println("hehe");

        return VsResponseUtil.ok(postService.createPostInGroup(idGroup, idUser, postDTO, fileImages, fileVideos));
    }

    @ApiOperation(value = "Chỉnh sửa bài đăng")
    @PatchMapping("/{idPost}/update-post")
    public ResponseEntity<?> updatePost(
            @PathVariable("idPost") Long id,
            @ModelAttribute PostDTO postDTO,
            @RequestParam(name = "fileImages", required = false) MultipartFile[] fileImages,
            @RequestParam(name = "fileVideos", required = false) MultipartFile[] fileVideos) {
        return VsResponseUtil.ok(postService.updatePost(id, postDTO, fileImages, fileVideos));
    }

    @ApiOperation(value = "Xoá bài đăng")
    @DeleteMapping("/{idPost}/delete-post")
    public ResponseEntity<?> deletePost(@PathVariable("idPost") Long id) {
        return VsResponseUtil.ok(postService.deletePostById(id));
    }

    @ApiOperation(value = "Lấy ra tất cả ảnh của post theo id.")
    @GetMapping("/{id}/images")
    public ResponseEntity<?> getAllImageById(@PathVariable Long id) {
        return VsResponseUtil.ok(postService.getAllImageById(id));
    }

    @ApiOperation(value = "Lấy ra tất cả video của post theo id.")
    @GetMapping("/{id}/videos")
    public ResponseEntity<?> getAllVideoById(@PathVariable Long id) {
        return VsResponseUtil.ok(postService.getAllVideoById(id));
    }

    @ApiOperation(value = "Reaction bài post.")
    @PostMapping("/{idPost}/{idReaction}/set-reaction")
    public ResponseEntity<?> setReactByIdPost(
            @PathVariable("idPost") Long idPost,
            @PathVariable("idReaction") Long idReaction
            ) {
        return VsResponseUtil.ok(postService.setReactByIdPost(idPost, idReaction));
    }

    @ApiOperation(value = "Lấy ra tất cả reaction bài post.")
    @GetMapping("/{idPost}/reactions")
    public ResponseEntity<?> getAllReactionByIdPost(
            @PathVariable("idPost") Long idPost
    ) {
        return VsResponseUtil.ok(postService.getAllReactionByIdPost(idPost));
    }

    @ApiOperation(value = "Lấy ra tất cả bình luận của bài post.")
    @GetMapping("/{idPost}/comments")
    public ResponseEntity<?> getAllCommentByIdPost(
            @PathVariable("idPost") Long idPost
    ) {
        return VsResponseUtil.ok(postService.getAllCommentByIdPost(idPost));
    }
}
