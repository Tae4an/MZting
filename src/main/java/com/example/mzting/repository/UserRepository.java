package com.example.mzting.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.mzting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByUsername(String username);

    @Query("SELECT u.username FROM User u WHERE u.id = :userId")
    String findUsernameById(@Param("userId") Long userId);
}
