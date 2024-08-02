package com.example.mzting.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 댓글 관련 DTO 클래스들을 정의
 */
public class CommentDTO {

    /**
     * 게시물 댓글 작성 요청 DTO 클래스
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPostsCommentsRequest {
        private Long userId;
        private String content;
        private Boolean isLike;
    }

    /**
     * 게시물 댓글 작성 응답 DTO 클래스
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPostsCommentsResponse {
        private Boolean success;
    }

    /**
     * 게시물 댓글 조회 요청 DTO 클래스
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPostsCommentsRequest {
        private int page;
        private int size;
    }

    /**
     * 게시물 댓글 조회 응답 DTO 클래스
     */
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

    /**
     * 댓글 정보 DTO 클래스
     */
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

    /**
     * 좋아요 및 싫어요 개수 정보 DTO 클래스
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikeDisLikeCountInfo {
        private long totalLikeCount;
        private long totalDislikeCount;
    }

    /**
     * 페이지네이션 정보 DTO 클래스
     */
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
