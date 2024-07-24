package com.example.mzting.repository;

import com.example.mzting.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    List<Profile> findByMbti(String mbti);

    @Query("SELECT p FROM Profile p WHERE p.mbti = :mbti ORDER BY FUNCTION('RAND')")
    Page<Profile> findRandomProfileByMbti(String mbti, Pageable pageable);
}
