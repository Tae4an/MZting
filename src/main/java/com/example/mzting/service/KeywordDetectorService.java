package com.example.mzting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeywordDetectorService {
    @Autowired
    private SituationService situationService;
    @Autowired
    private ChoiceService choiceService;

    private final List<String> appointmentKeywords = Arrays.asList("약속", "장소", "만날까요", "어디서");
    private final List<String> meetingKeywords = Arrays.asList("소개팅", "대화", "질문", "관심사", "취미");
    private final List<String> afterKeywords = Arrays.asList("다음", "또", "연락처", "번호", "만나기", "애프터");

    public Map<String, List<String>> detectKeywordsAndGetChoices(String text) {
        Map<String, List<String>> result = new HashMap<>();

        if (containsAnyKeyword(text, appointmentKeywords)) {
            result.put("APPOINTMENT_SETTING", situationService.getRandomPlaces(3));
        } else if (containsAnyKeyword(text, meetingKeywords)) {
            result.put("ACTUAL_MEETING", choiceService.getRandomChoices(2, 3));
        } else if (containsAnyKeyword(text, afterKeywords)) {
            result.put("AFTER_MEETING", choiceService.getRandomChoices(7, 3));
        }

        return result;
    }

    private boolean containsAnyKeyword(String text, List<String> keywords) {
        return keywords.stream().anyMatch(text::contains);
    }
}