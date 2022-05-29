//package com.example.strawberry.adapter.web.v1.controller;
//
//import com.example.strawberry.adapter.web.base.VsResponseUtil;
//import com.example.strawberry.application.service.IImageService;
//import com.example.strawberry.application.service.IPostService;
//import com.example.strawberry.domain.dto.PostDTO;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/api/v1/images")
//public class ImageController {
//
//    @Autowired
//    private IImageService imageService;
//
//
//    @ApiOperation(value = "Đăng ảnh")
//    @PostMapping("/{idPost}")
//    public ResponseEntity<?> uploadImage(
//            @PathVariable("idPost") Long id,
//            @RequestParam(name = "fileImage", required = false) MultipartFile fileImage) throws IOException {
//        return VsResponseUtil.ok(imageService.upImage(id, fileImage));
//    }
//}
