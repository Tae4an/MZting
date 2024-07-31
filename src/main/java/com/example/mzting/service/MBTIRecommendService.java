package com.example.mzting.service;

import com.example.mzting.entity.MBTICompatibility;
import com.example.mzting.repository.MBTICompatibilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mzting.dto.MBTIRecommendDTO;

import java.util.*;

import java.util.Optional;

@Service
public class MBTIRecommendService {
    private final MBTICompatibilityRepository repository;

    @Autowired
    public MBTIRecommendService(MBTICompatibilityRepository repository) {
        this.repository = repository;
    }

    public Optional<MBTIRecommendDTO.GetMBTIRecommendResponse> getCompatibilityByMBTI(String mbti) {
        return repository.findById(mbti.toUpperCase())
                .map(this::convertToGroupDTO);
    }

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