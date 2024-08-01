package com.example.mzting.repository;

import com.example.mzting.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByUserId(Long userId);
    List<ChatRoom> findByUserIdAndProfileId(Long userId, Integer profileId);
}