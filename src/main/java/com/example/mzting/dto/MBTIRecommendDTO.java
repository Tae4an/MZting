package com.example.mzting.dto;
import lombok.Data;
import java.util.List;
import java.util.Map;


public class MBTIRecommendDTO {

    @Data
    public static class GetMBTIRecommendResponse {
        private String mbti;
        private Map<String, List<String>> compatibilityGroups;
    }
}