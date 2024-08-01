package com.example.mzting.controller;

import com.example.mzting.entity.Profile;
import com.example.mzting.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 프로필 관련 요청을 처리하는 컨트롤러 클래스
 * 프로필 조회와 관련된 API 엔드포인트를 정의
 */
@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    // 프로필 서비스
    @Autowired
    private ProfileService profileService;

    /**
     * 모든 프로필을 조회하는 엔드포인트
     *
     * @return 모든 프로필 목록
     */
    @GetMapping
    public List<Profile> getProfiles() {
        return profileService.getAllProfiles();
    }

    /**
     * 특정 MBTI 유형에 해당하는 프로필을 조회하는 엔드포인트
     *
     * @param type MBTI 유형 문자열
     * @return 해당 MBTI 유형의 프로필 목록
     */
    @GetMapping("/mbti/{type}")
    public List<Profile> getProfilesByMbti(@PathVariable String type) {
        return profileService.getProfilesByMbti(type);
    }
}
