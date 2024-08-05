package com.example.mzting.controller;

import com.example.mzting.dto.MBTIRecommendDTO;
import com.example.mzting.service.MBTIRecommendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * MBTI 추천 관련 요청을 처리하는 컨트롤러 클래스
 * MBTI 호환성 추천과 관련된 API 엔드포인트를 정의
 */
@RestController
@RequestMapping("/api/recommend")
public class MBTIRecommendController {

    // MBTI 추천 서비스
    private final MBTIRecommendService mbtiRecommendService;

    /**
     * MBTIRecommendController 생성자
     * 필요한 의존성을 주입받아 초기화
     *
     * @param mbtiRecommendService MBTI 추천 서비스
     */
    public MBTIRecommendController(MBTIRecommendService mbtiRecommendService) {

        this.mbtiRecommendService = mbtiRecommendService;
    }

    /**
     * 주어진 MBTI에 대한 호환성을 조회하는 엔드포인트
     *
     * @param mbti MBTI 유형 문자열
     * @return 호환성 추천 결과를 포함한 ResponseEntity 객체
     */
    @GetMapping("/compatibility/{mbti}")
    public ResponseEntity<MBTIRecommendDTO.GetMBTIRecommendResponse> getCompatibility(@PathVariable String mbti) {
        return mbtiRecommendService.getCompatibilityByMBTI(mbti)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
