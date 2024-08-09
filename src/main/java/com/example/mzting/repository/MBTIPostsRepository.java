package com.example.mzting.repository;

import com.example.mzting.entity.MBTIPosts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MBTIPostsRepository extends JpaRepository<MBTIPosts, Long> {
    Optional<MBTIPosts> findByPostId(Long postId);

    @Modifying
    @Transactional
    @Query("UPDATE MBTIPosts m SET m.totalLikeCount = m.totalLikeCount + 1 WHERE m.postId = :postId")
    int incrementTotalLikeCount(@Param("postId") Long postId);

    @Modifying
    @Transactional
    @Query("UPDATE MBTIPosts m SET m.totalDislikeCount = m.totalDislikeCount + 1 WHERE m.postId = :postId")
    int incrementTotalDislikeCount(@Param("postId") Long postId);
}