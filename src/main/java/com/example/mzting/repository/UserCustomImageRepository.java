package com.example.mzting.repository;

import com.example.mzting.entity.MBTIPosts;
import com.example.mzting.entity.UserCustomImage;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCustomImageRepository extends JpaRepository<UserCustomImage, Long> {

}
