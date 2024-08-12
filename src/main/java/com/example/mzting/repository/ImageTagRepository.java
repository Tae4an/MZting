package com.example.mzting.repository;

import com.example.mzting.entity.ImageTag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImageTagRepository extends JpaRepository<ImageTag, Long> {
}
