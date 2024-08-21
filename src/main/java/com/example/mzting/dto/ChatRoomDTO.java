package com.example.mzting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public interface ChatRoomDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class ChatRoomListDTO {
        private long id;
        private boolean isProcessing;
        private String name;
        private int profileId;
        private String profileImage;
        private ChatRoomListResultDTO result;
        private long userId;
        private LocalDateTime createTime;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class ChatRoomListResultDTO {
        private int score;
        private String summaryEval;
        private String summaryFeel;
    }
}
