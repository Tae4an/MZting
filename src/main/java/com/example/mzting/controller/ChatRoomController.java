package com.example.mzting.controller;

import com.example.mzting.dto.ChatRoomRequest;
import com.example.mzting.dto.ChatRoomWithHistoryDTO;
import com.example.mzting.entity.ChatRoom;
import com.example.mzting.repository.UserRepository;
import com.example.mzting.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 채팅방 관련 요청을 처리하는 컨트롤러 클래스
 * 채팅방 생성, 목록 조회, 히스토리 조회 등의 API 엔드포인트를 제공
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatRoomController {

    // 채팅방 서비스
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;

    /**
     * ChatRoomController 생성자
     * 필요한 의존성을 주입받아 초기화
     *
     * @param chatRoomService 채팅방 서비스
     */
    public ChatRoomController(ChatRoomService chatRoomService, UserRepository userRepository) {
        this.chatRoomService = chatRoomService;
        this.userRepository = userRepository;
    }

    /**
     * 채팅방을 생성하는 엔드포인트
     * 주어진 요청 객체를 바탕으로 채팅방을 생성하고 반환
     *
     * @return 생성된 채팅방을 포함한 ResponseEntity 객체
     */
    @GetMapping("/chatroom/create/{profileId}")
    public ResponseEntity<ChatRoom> createChatRoom(@PathVariable Integer profileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chatRoomName = "채팅방";
        long uid = 1;
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println(username);
            uid = userRepository.findIdByUsername(username);
            chatRoomName = username + "와 " + profileId + "번 프로필의 채팅방";
        }

        ChatRoomRequest request = new ChatRoomRequest(chatRoomName, uid, profileId);

        ChatRoom chatRoom = chatRoomService.createChatRoom(request);
        return ResponseEntity.ok(chatRoom);
    }

    /**
     * 사용자 ID에 따른 채팅방 목록을 조회하는 엔드포인트
     *
     * @return 채팅방 목록을 포함한 ResponseEntity 객체
     */
    @GetMapping("/chatroom/list/all")
    public ResponseEntity<List<ChatRoom>> getAllChatRoomList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long uid = 1;
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            uid = userRepository.findIdByUsername(username);
        }

        List<ChatRoom> chatRooms = chatRoomService.getChatRoomsByUserId(uid);
        return ResponseEntity.ok(chatRooms);
    }

    @GetMapping("/chatroom/list/{profileId}")
    public ResponseEntity<List<ChatRoom>> getChatRoomListByProfileId(@PathVariable Long profileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long uid = 1;
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            uid = userRepository.findIdByUsername(username);
        }

        List<ChatRoom> chatRooms = chatRoomService.getChatRoomsByUserId(uid);
        return ResponseEntity.ok(chatRooms);
    }


    /**
     * 특정 채팅방의 히스토리를 조회하는 엔드포인트
     *
     * @param chatRoomId 채팅방 ID
     * @return 채팅방과 히스토리를 포함한 ChatRoomWithHistoryDTO 객체를 담은 ResponseEntity 객체
     */
    @GetMapping("/chatroom/{chatRoomId}")
    public ResponseEntity<ChatRoomWithHistoryDTO> getChatRoomWithHistory(@PathVariable Long chatRoomId) {
        // 채팅방이 유저의 것인지 검증하는 로직 추가 필요
        ChatRoomWithHistoryDTO chatRoomWithHistory = chatRoomService.getChatRoomWithHistory(chatRoomId);
        return ResponseEntity.ok(chatRoomWithHistory);
    }
}
