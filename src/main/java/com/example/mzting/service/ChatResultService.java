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

@Service
public class ChatResultService {
    private final ChatRepository chatRepository;
    private final ObjectMapper objectMapper;

    public ChatResultService(ChatRepository chatRepository, ObjectMapper objectMapper) {
        this.chatRepository = chatRepository;
        this.objectMapper = objectMapper;
    }

    public ChatResultResponse getBotMessagesWithSummary(Long chatRoomId) {
        List<Chat> botChats = chatRepository.findByChatRoomIdAndIsBotTrueOrderBySendAtAsc(chatRoomId);

        int averageScore = (int) Math.round(calculateAverageScore(botChats));
        String summarizedFeel = summarizeFeel(botChats);
        String summarizedEvaluation = summarizeEvaluation(botChats);

        return new ChatResultResponse(averageScore, summarizedEvaluation, summarizedFeel);
    }

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