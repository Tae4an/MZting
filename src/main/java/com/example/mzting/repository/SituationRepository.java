package com.example.mzting.repository;

import com.example.mzting.entity.Situation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SituationRepository extends JpaRepository<Situation, Integer> {
}