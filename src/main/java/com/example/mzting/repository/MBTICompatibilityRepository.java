package com.example.mzting.repository;

import com.example.mzting.entity.MBTICompatibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MBTICompatibilityRepository extends JpaRepository<MBTICompatibility, String> {

}