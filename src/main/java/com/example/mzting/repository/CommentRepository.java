package com.example.mzting.repository;

import com.example.mzting.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByProfileId(Long profileId, Pageable pageable);

    @Query("SELECT c FROM Comment c " +
            "WHERE c.profileId = :profileId AND c.isLike = :isLike")
    Page<Comment> findLikedOrDislikedCommentsByProfileId(
            @Param("profileId") Long profileId,
            @Param("isLike") Boolean isLike,
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount + 1 WHERE c.commentId = :commentId")
    int incrementLikeCount(@Param("commentId") Long commentId);

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount - 1 WHERE c.commentId = :commentId")
    int decrementLikeCount(@Param("commentId") Long commentId);

    @Modifying
    @Query("UPDATE Comment c SET c.dislikeCount = c.dislikeCount + 1 WHERE c.commentId = :commentId")
    int incrementDisLikeCount(@Param("commentId") Long commentId);

    @Modifying
    @Query("UPDATE Comment c SET c.dislikeCount = c.dislikeCount - 1 WHERE c.commentId = :commentId")
    int decrementDisLikeCount(@Param("commentId") Long commentId);
}