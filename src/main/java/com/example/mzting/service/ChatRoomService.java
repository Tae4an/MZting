package com.example.mzting.service;

import com.example.mzting.dto.ChatRoomDTO;
import com.example.mzting.dto.ChatRoomEntryResponseDTO;
import com.example.mzting.dto.ChatRoomRequest;
import com.example.mzting.dto.ChatRoomWithHistoryDTO;
import com.example.mzting.entity.Chat;
import com.example.mzting.entity.ChatRoom;
import com.example.mzting.entity.UserCustomImage;
import com.example.mzting.exception.ResourceNotFoundException;
import com.example.mzting.repository.ChatRepository;
import com.example.mzting.repository.ChatRoomRepository;
import com.example.mzting.repository.UserCustomImageRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserCustomImageRepository userCustomImageRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, ChatRepository chatRepository, UserCustomImageRepository userCustomImageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRepository = chatRepository;
        this.userCustomImageRepository = userCustomImageRepository;
    }

    public ChatRoom createChatRoom(ChatRoomRequest request) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(request.getName());
        chatRoom.setUserId(request.getUserId());
        chatRoom.setProfileId(request.getProfileId());
        chatRoom.setMissionFlag(0);
        chatRoom.setIsProcessing(true);
        chatRoom.setResult(null);
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoomWithHistoryDTO getChatRoomWithHistory(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom not found with id: " + chatRoomId));
        List<Chat> chatHistory = chatRepository.findByChatRoomIdOrderBySendAtAsc(chatRoomId);
        return new ChatRoomWithHistoryDTO(chatRoom, chatHistory);
    }

    public List<ChatRoomEntryResponseDTO> getChatRoomEntries(Long chatRoomId) {
        List<Chat> chatHistory = chatRepository.findByChatRoomIdOrderBySendAtAsc(chatRoomId);
        List<ChatRoomEntryResponseDTO> chatRoomEntryResponseDTOs = new ArrayList<>();

        for (Chat chat : chatHistory) {
            String role = chat.getIsBot() ? "assistant" : "user";
            ChatRoomEntryResponseDTO dto = new ChatRoomEntryResponseDTO(
                    role,
                    chat.getContent(),
                    chat.getBotInfo()  // botInfo 추가
            );
            chatRoomEntryResponseDTOs.add(dto);
        }

        return chatRoomEntryResponseDTOs;
    }

    public List<ChatRoomDTO.ChatRoomListDTO> getChatRoomsByUserId(Long userId) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findByUserId(userId);
        Optional<UserCustomImage> userCustomImage = userCustomImageRepository.findById(userId);
        List<ChatRoomDTO.ChatRoomListDTO> chatRoomDTOList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for(ChatRoom chatRoom : chatRoomList) {
            ChatRoomDTO.ChatRoomListDTO tcrl = new ChatRoomDTO.ChatRoomListDTO();
            tcrl.setId(chatRoom.getId());
            tcrl.setName(chatRoom.getName());
            tcrl.setUserId(chatRoom.getUserId());
            tcrl.setProfileId(chatRoom.getProfileId());
            tcrl.setCreateTime(chatRoom.getCreateTime());
            tcrl.setProcessing(chatRoom.getIsProcessing());
            String profileImageUrl = userCustomImage
                    .map(uci -> {
                        try {
                            int profileIndex = Integer.parseInt(String.valueOf(chatRoom.getProfileId()));
                            return uci.getProfileImage(profileIndex);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid profile ID: " + chatRoom.getProfileId());
                            return uci.getProfileImage(1); // 기본 이미지 반환
                        }
                    })
                    .orElse("https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile1.jpg?alt=media&token=d9a25fd3-78d0-480b-8f94-249287cd80ad");
            tcrl.setProfileImage(profileImageUrl);

            // 결과 파싱 및 설정
            String resultJson = chatRoom.getResult();
            if (resultJson != null && !resultJson.isEmpty()) {
                try {
                    Map<String, Object> resultMap = objectMapper.readValue(resultJson, new TypeReference<Map<String, Object>>() {});
                    ChatRoomDTO.ChatRoomListResultDTO resultDTO = new ChatRoomDTO.ChatRoomListResultDTO();
                    resultDTO.setScore((Integer) resultMap.get("score"));
                    resultDTO.setSummaryEval((String) resultMap.get("summaryEval"));
                    resultDTO.setSummaryFeel((String) resultMap.get("summaryFeel"));
                    tcrl.setResult(resultDTO);
                } catch (Exception e) {
                    // JSON 파싱 실패 시 로그 기록
                    System.err.println("Failed to parse result JSON: " + e.getMessage());
                    // 결과를 null로 설정하거나 기본값 사용
                    tcrl.setResult(null);
                }
            } else {
                tcrl.setResult(null);
            }

            chatRoomDTOList.add(tcrl);
        }
        return chatRoomDTOList;
    }

    public List<ChatRoom> getChatRoomsByUserIdAndProfileId(Long userId, Integer profileId) {
        return chatRoomRepository.findByUserIdAndProfileId(userId, profileId);
    }

    public boolean isUserAuthorized(Long userId, Long chatRoomId) {
        return chatRoomRepository.existsByIdAndUserId(chatRoomId, userId);
    }
}