package com.example.mzting.controller;

import com.example.mzting.DTO.CommentDTO;
import com.example.mzting.entity.Comment;
import com.example.mzting.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDTO.PostPostsCommentsResponse> createComment(
            @PathVariable Long postId,
            @RequestBody CommentDTO.PostPostsCommentsRequest PostPostsCommentRequest) {
        try {
            Comment comment = new Comment();
            comment.setUserId(PostPostsCommentRequest.getUserId());
            comment.setPostId(postId);
            comment.setContent(PostPostsCommentRequest.getContent());
            comment.setIsLike(PostPostsCommentRequest.getIsLike());

            Comment savedComment = commentService.saveComment(comment);

            CommentDTO.PostPostsCommentsResponse response = new CommentDTO.PostPostsCommentsResponse();
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating comment: ", e);
            CommentDTO.PostPostsCommentsResponse response = new CommentDTO.PostPostsCommentsResponse();
            response.setSuccess(false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<CommentDTO.GetPostsCommentsResponse> getCommentsByPostId(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        CommentDTO.GetPostsCommentsResponse response = commentService.getCommentInfoByPostId(postId, PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }
}