package com.example.mzting.controller;

import com.example.mzting.entity.MBTICompatibility;
import com.example.mzting.DTO.MBTIRecommendDTO;
import com.example.mzting.service.MBTIRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommend")
public class MBTIRecommendController {
    private final MBTIRecommendService service;

    @Autowired
    public MBTIRecommendController(MBTIRecommendService service) {
        this.service = service;
    }

    @GetMapping("/compatibility/{mbti}")
    public ResponseEntity<MBTIRecommendDTO.GetMBTIRecommendResponse> getCompatibility(@PathVariable String mbti) {
        return service.getCompatibilityByMBTI(mbti)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
