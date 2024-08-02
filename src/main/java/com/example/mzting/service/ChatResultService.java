package com.example.mzting.service;

import com.example.mzting.dto.ChatResultResponse;
import com.example.mzting.entity.Chat;
import com.example.mzting.repository.ChatRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * 채팅 결과 서비스 클래스
 * 채팅방의 봇 메시지와 관련된 처리 및 요약을 담당
 */
@Service
public class ChatResultService {

    // 채팅 저장소
    private final ChatRepository chatRepository;

    // 객체 매퍼
    private final ObjectMapper objectMapper;

    /**
     * ChatResultService 생성자
     * 필요한 의존성을 주입받아 초기화
     *
     * @param chatRepository 채팅 저장소
     * @param objectMapper 객체 매퍼
     */
    public ChatResultService(ChatRepository chatRepository, ObjectMapper objectMapper) {
        this.chatRepository = chatRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * 채팅방 ID를 기반으로 봇 메시지와 요약을 가져오는 메서드
     *
     * @param chatRoomId 채팅방 ID
     * @return 봇 메시지와 요약 정보를 포함한 ChatResultResponse 객체
     */
    public ChatResultResponse getBotMessagesWithSummary(Long chatRoomId) {
        List<Chat> botChats = chatRepository.findByChatRoomIdAndIsBotTrueOrderBySendAtAsc(chatRoomId);

        int averageScore = (int) Math.round(calculateAverageScore(botChats));
        String summarizedFeel = summarizeFeel(botChats);
        String summarizedEvaluation = summarizeEvaluation(botChats);

        return new ChatResultResponse(averageScore, summarizedEvaluation, summarizedFeel);
    }

    /**
     * 채팅 메시지의 평균 점수를 계산하는 메서드
     *
     * @param chats 채팅 메시지 리스트
     * @return 평균 점수
     */
    private double calculateAverageScore(List<Chat> chats) {
        OptionalDouble average = chats.stream()
                .mapToDouble(chat -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(chat.getBotInfo());
                        return jsonNode.get("score").asDouble();
                    } catch (Exception e) {
                        return 0.0;
                    }
                })
                .average();
        return average.orElse(0.0);
    }

    /**
     * 채팅 메시지의 느낌을 요약하는 메서드
     *
     * @param chats 채팅 메시지 리스트
     * @return 요약된 느낌 문자열
     */
    private String summarizeFeel(List<Chat> chats) {
        return chats.stream()
                .map(chat -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(chat.getBotInfo());
                        return jsonNode.get("feel").asText();
                    } catch (Exception e) {
                        return "";
                    }
                })
                .collect(Collectors.joining(", "));
    }

    /**
     * 채팅 메시지의 평가를 요약하는 메서드
     *
     * @param chats 채팅 메시지 리스트
     * @return 요약된 평가 문자열
     */
    private String summarizeEvaluation(List<Chat> chats) {
        return chats.stream()
                .map(chat -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(chat.getBotInfo());
                        return jsonNode.get("evaluation").asText();
                    } catch (Exception e) {
                        return "";
                    }
                })
                .collect(Collectors.joining(" "));
    }
}
