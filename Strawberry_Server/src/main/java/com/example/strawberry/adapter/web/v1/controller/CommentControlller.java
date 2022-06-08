package com.example.strawberry.adapter.web.v1.controller;

import com.example.strawberry.adapter.web.base.RestApiV1;
import com.example.strawberry.adapter.web.base.VsResponseUtil;
import com.example.strawberry.application.constants.UrlConstant;
import com.example.strawberry.application.service.ICommentService;
import com.example.strawberry.application.service.Impl.CommentServiceImpl;
import com.example.strawberry.domain.dto.CommentDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestApiV1
public class CommentControlller {

    private final ICommentService commentService;

    public CommentControlller(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(value = "Bình luận bài viết")
    @PostMapping(UrlConstant.Comment.DATA_COMMENT_WRITE_IN_POST)
    public ResponseEntity<?> createCommentForPost(
            @PathVariable("idPost") Long idPost,
            @RequestBody @Valid CommentDTO commentDTO) {
        return VsResponseUtil.ok(commentService.createCommentForPost(idPost, commentDTO));
    }

    @ApiOperation(value = "Bình luận của bình luận")
    @PostMapping(UrlConstant.Comment.DATA_COMMENT_WRITE_IN_COMMENT)
    public ResponseEntity<?> createCommentForComment(
            @PathVariable("idCommentParent") Long idCommentParent,
            @RequestBody @Valid CommentDTO commentDTO) {
        return VsResponseUtil.ok(commentService.createCommentForComment(idCommentParent, commentDTO));
    }

    @ApiOperation(value = "Cập nhật bình luận")
    @PatchMapping(UrlConstant.Comment.DATA_COMMENT_UPDATE_COMMENT)
    public ResponseEntity<?> updateCommentForPost(
            @PathVariable("idComment") Long idComment,
            @RequestBody @Valid CommentDTO commentDTO) {
        return VsResponseUtil.ok(commentService.updateCommentForPost(idComment, commentDTO));
    }

    @ApiOperation(value = "Xoá bình luận")
    @DeleteMapping(UrlConstant.Comment.DATA_COMMENT_DETETE_COMMENT)
    public ResponseEntity<?> deleteCommentForPost(@PathVariable("idComment") Long idComment) {
        return VsResponseUtil.ok(commentService.deleteCommentForPost(idComment));
    }

    @ApiOperation(value = "Lấy bình luận theo id bình luận cha")
    @GetMapping(UrlConstant.Comment.DATA_COMMENT_GET_COMMENT_CHILD)
    public ResponseEntity<?> getAllCommentChild(
            @PathVariable("idCommentParent") Long idCommentParent) {
        return VsResponseUtil.ok(commentService.getAllCommentChild(idCommentParent));
    }
}
