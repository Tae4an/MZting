package com.example.mzting.controller;

import com.example.mzting.dto.ChatResultResponse;
import com.example.mzting.service.ChatResultService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 채팅 결과 관련 요청을 처리하는 컨트롤러 클래스
 * 채팅 결과 요약을 제공하는 API 엔드포인트를 정의
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatResultController {

    // 채팅 결과 서비스
    private final ChatResultService chatResultService;

    /**
     * ChatResultController 생성자
     * 필요한 의존성을 주입받아 초기화
     *
     * @param chatResultService 채팅 결과 서비스
     */
    public ChatResultController(ChatResultService chatResultService) {
        this.chatResultService = chatResultService;
    }

    /**
     * 채팅 요약을 가져오는 엔드포인트
     * 주어진 채팅방 ID에 대한 봇 메시지와 요약을 반환
     *
     * @param chatRoomId 채팅방 ID
     * @return 채팅 요약을 포함한 ChatResultResponse 객체
     */
    @GetMapping("chat/result/{chatRoomId}")
    public ChatResultResponse getChatSummary(@PathVariable Long chatRoomId, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("uid");
        // chatRoom이 자신의 chatRoom인지 검증하는 로직 필요
        return chatResultService.getBotMessagesWithSummary(chatRoomId);
    }
}
