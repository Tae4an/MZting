package com.example.mzting.repository;

import com.example.mzting.DTO.LikeDisLikeCountInfo;
import com.example.mzting.entity.MBTIPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MBTIPostsRepository extends JpaRepository<MBTIPosts, Long> {
    Optional<MBTIPosts> findByPostId(Long postId);
}