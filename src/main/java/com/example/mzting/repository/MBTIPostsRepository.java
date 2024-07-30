package com.example.mzting.repository;

import com.example.mzting.entity.MBTIPosts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MBTIPostsRepository extends JpaRepository<MBTIPosts, Long> {
    Optional<MBTIPosts> findByPostId(Long postId);
}