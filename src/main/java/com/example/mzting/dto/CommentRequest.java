package com.example.mzting.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private Long userId;
    private Long postId;
    private String content;
    private Boolean isLike;
}