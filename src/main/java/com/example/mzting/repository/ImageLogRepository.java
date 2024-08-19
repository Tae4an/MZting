package com.example.mzting.repository;

import com.example.mzting.entity.ImageLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageLogRepository extends JpaRepository<ImageLog, Long> {
    List<ImageLog> findAllByUserId(Long userId);
}
