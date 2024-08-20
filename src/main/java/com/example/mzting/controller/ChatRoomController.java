package com.example.mzting.controller;

import com.example.mzting.dto.ChatRoomEntryResponseDTO;
import com.example.mzting.dto.ChatRoomRequest;
import com.example.mzting.dto.ChatRoomWithHistoryDTO;
import com.example.mzting.entity.ChatRoom;
import com.example.mzting.service.ChatRoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * ChatRoomController 생성자
     * 필요한 의존성을 주입받아 초기화
     *
     * @param chatRoomService 채팅방 서비스
     */
    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    /**
     * 채팅방을 생성하는 엔드포인트
     * 주어진 요청 객체를 바탕으로 채팅방을 생성하고 반환
     *
     * @return 생성된 채팅방을 포함한 ResponseEntity 객체
     */
    @GetMapping("/chatroom/create/{profileId}")
    public ResponseEntity<Long> createChatRoom(@PathVariable Integer profileId, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("uid");
        String username = (String) request.getAttribute("username");

        String chatRoomName = username + "과(와) " + profileId + "번 프로필의 채팅방";

        ChatRoomRequest chatRoomRequest = new ChatRoomRequest(chatRoomName, uid, profileId);

        ChatRoom chatRoom = chatRoomService.createChatRoom(chatRoomRequest);
        return ResponseEntity.ok(chatRoom.getId());
    }

    /**
     * 사용자 ID에 따른 채팅방 목록을 조회하는 엔드포인트
     *
     * @return 채팅방 목록을 포함한 ResponseEntity 객체
     */
    @GetMapping("/chatroom/list/all")
    public ResponseEntity<List<ChatRoom>> getAllChatRoomList(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("uid");

        List<ChatRoom> chatRooms = chatRoomService.getChatRoomsByUserId(uid);
        return ResponseEntity.ok(chatRooms);
    }

    @GetMapping("/chatroom/list/{profileId}")
    public ResponseEntity<List<ChatRoom>> getChatRoomListByProfileId(@PathVariable Integer profileId, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("uid");

        List<ChatRoom> chatRooms = chatRoomService.getChatRoomsByUserIdAndProfileId(uid, profileId);
        return ResponseEntity.ok(chatRooms);
    }

    /**
     * 특정 채팅방의 히스토리를 조회하는 엔드포인트
     *
     * @param chatRoomId 채팅방 ID
     * @return 채팅방과 히스토리를 포함한 ChatRoomWithHistoryDTO 객체를 담은 ResponseEntity 객체
     */
    @GetMapping("/chatroom/{chatRoomId}")
    public ResponseEntity<?> getChatRoomWithHistory(@PathVariable Long chatRoomId, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("uid");

        // 채팅방이 유저의 것인지 검증
        if (!chatRoomService.isUserAuthorized(uid, chatRoomId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: You are not authorized to view this chat room.");
        }

        ChatRoomWithHistoryDTO chatRoomWithHistory = chatRoomService.getChatRoomWithHistory(chatRoomId);
        return ResponseEntity.ok(chatRoomWithHistory);
    }

    @GetMapping("/chatroom/entry/{chatRoomId}")
    public ResponseEntity<?> getChatRoomChat(@PathVariable Long chatRoomId, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("uid");

        // 채팅방이 유저의 것인지 검증
        if (!chatRoomService.isUserAuthorized(uid, chatRoomId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: You are not authorized to view this chat room.");
        }

        List<ChatRoomEntryResponseDTO> chatRoomEntryResponseDTOs = chatRoomService.getChatRoomEntries(chatRoomId);
        return ResponseEntity.ok(chatRoomEntryResponseDTOs);
    }
}