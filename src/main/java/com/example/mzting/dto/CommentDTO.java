package com.example.mzting.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;


public class CommentDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPostsCommentsRequest {
        private Long userId;
        private String content;
        private Boolean isLike;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPostsCommentsResponse {
        private Boolean success;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPostsCommentsRequest {
        private int page;
        private int size;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPostsCommentsResponse {
        private long totalLikeCount;
        private long totalDislikeCount;
        private List<CommentInfo> commentInfos;
        private PaginationInfo paginationInfo;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentInfo {
        private String content;
        private Boolean isLike;
        private LocalDateTime cwTime;
        private String username;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikeDisLikeCountInfo {
        private long totalLikeCount;
        private long totalDislikeCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaginationInfo {
        private int currentPage;
        private int totalPages;
        private long totalElements;
        private int size;
    }
}