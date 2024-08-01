package com.example.mzting.service;

import com.example.mzting.entity.Chat;
import com.example.mzting.repository.ChatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * ChatService 클래스
 * 사용자와 Claude AI 간의 대화를 관리하는 서비스 클래스.
 * 사용자 요청과 AI 응답을 저장하고, 전체 대화를 반환하며,
 * Claude API에 전달할 메시지 형식으로 변환하는 기능을 제공.
 */
@Service
public class ChatService {

    // 채팅 저장소
    private final ChatRepository chatRepository;

    // 객체 매퍼
    private final ObjectMapper objectMapper;

    // 저장된 응답 리스트
    @Getter
    private List<String> savedResponses = new ArrayList<>();

    // 사용자 요청 리스트
    private List<String> userRequests = new ArrayList<>();

    // 컨텍스트 맵
    private Map<String, String> context = new HashMap<>();

    // 초기화된 채팅방 맵
    private Map<Long, Boolean> initializedChats = new HashMap<>();

    /**
     * ChatService 생성자
     * 필요한 의존성을 주입받아 초기화
     *
     * @param chatRepository 채팅 저장소
     * @param objectMapper 객체 매퍼
     */
    public ChatService(ChatRepository chatRepository, ObjectMapper objectMapper) {
        this.chatRepository = chatRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * 사용자 요청을 추가하는 메서드
     *
     * @param request 사용자 요청 문자열
     */
    public void addUserRequest(String request) {
        userRequests.add(request);
    }

    /**
     * Claude의 응답을 추가하는 메서드
     *
     * @param response Claude의 응답 문자열
     */
    public void addClaudeResponse(String response) {
        savedResponses.add(response);
    }

    /**
     * 전체 대화를 반환하는 메서드
     *
     * @return 사용자 요청과 Claude 응답이 순서대로 포함된 대화 리스트
     */
    public List<String> getConversation() {
        List<String> conversation = new ArrayList<>();
        for (int i = 0; i < userRequests.size(); i++) {
            conversation.add("User: " + userRequests.get(i));
            if (i < savedResponses.size()) {
                conversation.add("Claude: " + savedResponses.get(i));
            }
        }
        return conversation;
    }

    /**
     * Claude API에 전달할 메시지 형식으로 변환하는 메서드
     *
     * @return 사용자 요청과 Claude 응답을 포함한 메시지 리스트
     */
    public List<Map<String, String>> getMessagesForClaudeApi() {
        List<Map<String, String>> messages = new ArrayList<>();
        for (int i = 0; i < userRequests.size(); i++) {
            messages.add(Map.of("role", "user", "content", userRequests.get(i)));
            if (i < savedResponses.size()) {
                messages.add(Map.of("role", "assistant", "content", savedResponses.get(i)));
            }
        }
        return messages;
    }

    /**
     * 사용자 메시지를 저장하는 메서드
     *
     * @param content 메시지 내용
     * @param chatRoomId 채팅방 ID
     * @return 저장된 Chat 객체
     */
    public Chat saveUserMessage(String content, Long chatRoomId) {
        Chat chat = new Chat();
        chat.setChatRoomId(chatRoomId);
        chat.setIsBot(false);
        chat.setContent(content);
        chat.setBotInfo(null);
        return chatRepository.save(chat);
    }

    /**
     * Claude의 응답 메시지를 저장하는 메서드
     *
     * @param content Claude의 응답 객체
     * @param chatRoomId 채팅방 ID
     * @return 저장된 Chat 객체
     */
    public Chat saveUserMessage(com.example.mzting.dto.ClaudeResponse content, Long chatRoomId) {
        Chat chat = new Chat();
        chat.setChatRoomId(chatRoomId);
        chat.setIsBot(true);
        chat.setContent(content.getText());
        try {
            // ClaudeResponse 객체를 JSON으로 변환
            String jsonString = objectMapper.writeValueAsString(content);
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            // text 필드 제거
            ((ObjectNode) jsonNode).remove("text");
            ((ObjectNode) jsonNode).remove("errorMessage");

            // 남은 정보를 botInfo에 저장
            chat.setBotInfo(jsonNode.toString());
        } catch (JsonProcessingException e) {
            chat.setBotInfo(null);
            e.printStackTrace();
        }

        return chatRepository.save(chat);
    }

    /**
     * 컨텍스트를 추가하는 메서드
     *
     * @param key 컨텍스트 키
     * @param value 컨텍스트 값
     */
    public void addContext(String key, String value) {
        context.put(key, value);
    }

    /**
     * 특정 컨텍스트 값을 가져오는 메서드
     *
     * @param key 컨텍스트 키
     * @return 컨텍스트 값
     */
    public String getContext(String key) {
        return context.getOrDefault(key, "");
    }

    /**
     * 전체 컨텍스트를 반환하는 메서드
     *
     * @return 전체 컨텍스트 맵
     */
    public Map<String, String> getFullContext() {
        return new HashMap<>(context);
    }

    /**
     * 채팅방이 초기화되었는지 확인하는 메서드
     *
     * @param chatRoomId 채팅방 ID
     * @return 초기화 여부
     */
    public boolean isInitialized(Long chatRoomId) {
        return initializedChats.getOrDefault(chatRoomId, false);
    }

    /**
     * 채팅방의 초기화 상태를 설정하는 메서드
     *
     * @param chatRoomId 채팅방 ID
     * @param initialized 초기화 상태
     */
    public void setInitialized(Long chatRoomId, boolean initialized) {
        initializedChats.put(chatRoomId, initialized);
    }
}
