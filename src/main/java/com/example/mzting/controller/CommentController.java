package com.example.mzting.controller;

import com.example.mzting.DTO.CommentRequest;
import com.example.mzting.entity.Comment;
import com.example.mzting.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/save")
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setUserId(commentRequest.getUserId());
        comment.setPostId(commentRequest.getPostId());
        comment.setContent(commentRequest.getContent());
        comment.setIsLike(commentRequest.getIsLike());

        Comment savedComment = commentService.saveComment(comment);
        return ResponseEntity.ok(savedComment);
    }

    @GetMapping("/load")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@RequestParam Long postKey) {
        List<Comment> comments = commentService.getCommentsByPostId(postKey);
        return ResponseEntity.ok(comments);
    }
}
