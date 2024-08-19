package com.example.mzting.repository;

import com.example.mzting.entity.ImageLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageLogRepository extends JpaRepository<ImageLog, Long> {
    Page<ImageLog> findAllByUserId(Long userId, Pageable pageable);
}