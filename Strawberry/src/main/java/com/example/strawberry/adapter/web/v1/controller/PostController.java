//package com.example.strawberry.adapter.web.v1.controller;
//
//import com.example.strawberry.adapter.web.base.VsResponseUtil;
//import com.example.strawberry.application.service.IPostService;
//import com.example.strawberry.domain.dto.ActiveDTO;
//import com.example.strawberry.domain.dto.PostDTO;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/posts")
//public class PostController {
//
//    @Autowired
//    private IPostService postService;
//
//
//    @ApiOperation(value = "Đăng bài")
//    @PostMapping("/{idUser}")
//    public ResponseEntity<?> createPost(@PathVariable("idUser") Long id, @RequestBody PostDTO postDTO) {
//        return VsResponseUtil.ok(postService.createPost(id, postDTO));
//    }
//
//    @ApiOperation(value = "Chỉnh sửa bài đăng")
//    @PostMapping("/{idPost}")
//    public ResponseEntity<?> updatePost(@PathVariable("idPost") Long id, @RequestBody PostDTO postDTO) {
//        return VsResponseUtil.ok(postService.updatePost(id, postDTO));
//    }
//
//
//
//    @ApiOperation(value = "Lấy ra tất cả ảnh của post theo id.")
//    @GetMapping("/{id}/images")
//    public ResponseEntity<?> getAllImageById(@PathVariable Long id) {
//        return VsResponseUtil.ok(postService.getAllImageById(id));
//    }
//
//
//
//}
