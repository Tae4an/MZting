package com.example.mzting.service;

import com.example.mzting.entity.MBTICompatibility;
import com.example.mzting.repository.MBTICompatibilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mzting.dto.MBTIRecommendDTO;

import java.util.*;

/**
 * MBTIRecommendService 클래스
 * MBTI 유형 간의 호환성을 관리하는 서비스 클래스
 */
@Service
public class MBTIRecommendService {

    // MBTI 호환성 저장소
    private final MBTICompatibilityRepository repository;

    /**
     * MBTIRecommendService 생성자
     * 필요한 의존성을 주입받아 초기화
     *
     * @param repository MBTI 호환성 저장소
     */
    @Autowired
    public MBTIRecommendService(MBTICompatibilityRepository repository) {
        this.repository = repository;
    }

    /**
     * 특정 MBTI 유형의 호환성을 가져오는 메서드
     *
     * @param mbti MBTI 유형
     * @return 호환성 정보를 포함한 Optional 객체
     */
    public Optional<MBTIRecommendDTO.GetMBTIRecommendResponse> getCompatibilityByMBTI(String mbti) {
        return repository.findById(mbti.toUpperCase())
                .map(this::convertToGroupDTO);
    }

    /**
     * MBTI 호환성 엔티티를 DTO로 변환하는 메서드
     *
     * @param entity MBTI 호환성 엔티티
     * @return 변환된 GetMBTIRecommendResponse DTO
     */
    private MBTIRecommendDTO.GetMBTIRecommendResponse convertToGroupDTO(MBTICompatibility entity) {
        MBTIRecommendDTO.GetMBTIRecommendResponse dto = new MBTIRecommendDTO.GetMBTIRecommendResponse();
        dto.setMbti(entity.getMbti());

        Map<String, List<String>> groups = new HashMap<>();
        groups.put("worst", new ArrayList<>());
        groups.put("normal", new ArrayList<>());
        groups.put("good", new ArrayList<>());
        groups.put("soulMate", new ArrayList<>());

        addToGroup(groups, "infp", entity.getInfp());
        addToGroup(groups, "enfp", entity.getEnfp());
        addToGroup(groups, "infj", entity.getInfj());
        addToGroup(groups, "enfj", entity.getEnfj());
        addToGroup(groups, "intj", entity.getIntj());
        addToGroup(groups, "entj", entity.getEntj());
        addToGroup(groups, "intp", entity.getIntp());
        addToGroup(groups, "entp", entity.getEntp());
        addToGroup(groups, "isfp", entity.getIsfp());
        addToGroup(groups, "esfp", entity.getEsfp());
        addToGroup(groups, "istp", entity.getIstp());
        addToGroup(groups, "estp", entity.getEstp());
        addToGroup(groups, "isfj", entity.getIsfj());
        addToGroup(groups, "esfj", entity.getEsfj());
        addToGroup(groups, "istj", entity.getIstj());
        addToGroup(groups, "estj", entity.getEstj());

        dto.setCompatibilityGroups(groups);
        return dto;
    }

    /**
     * 주어진 MBTI 유형을 호환성 그룹에 추가하는 메서드
     *
     * @param groups 호환성 그룹 맵
     * @param mbtiType MBTI 유형
     * @param value 호환성 값
     */
    private void addToGroup(Map<String, List<String>> groups, String mbtiType, int value) {
        String group;
        switch (value) {
            case 0:
                group = "worst";
                break;
            case 1:
                group = "normal";
                break;
            case 2:
                group = "good";
                break;
            case 3:
                group = "soulMate";
                break;
            default:
                return; // 알 수 없는 값은 무시
        }
        groups.get(group).add(mbtiType);
    }
}
