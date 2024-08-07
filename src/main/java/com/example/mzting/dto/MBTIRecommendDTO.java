package com.example.mzting.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * MBTI 추천 관련 DTO 클래스
 */
public class MBTIRecommendDTO {

    /**
     * MBTI 추천 응답 DTO 클래스
     */
    @Data
    public static class GetMBTIRecommendResponse {

        // MBTI 유형
        private String mbti;

        // 호환성 그룹 맵
        private Map<String, List<String>> compatibilityGroups;
    }
}
