package com.example.mzting.controller;


import com.example.mzting.dto.CommentDTO;
import com.example.mzting.dto.GenerateImageDTO;
import com.example.mzting.entity.ImageTag;
import com.example.mzting.service.GenerateImageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gnimage")
public class GenerateImageController {

    private final GenerateImageService generateImageService;

    public GenerateImageController(GenerateImageService generateImageService) {
        this.generateImageService = generateImageService;
    }

    // 이미지 생성을 위한 태그를 불러오는 엔드포인트
    @GetMapping("/tag")
    public ResponseEntity<?> getTag() {
        return ResponseEntity.ok(generateImageService.getImageTags());
    }

    // 이미지 생성 및 저장, 반환 엔드포인트
    @PostMapping("/generate")
    public ResponseEntity<?> generateImage(@RequestBody GenerateImageDTO.GenerateImageRequest generateImageRequest, HttpServletRequest request) {
        return ResponseEntity.ok(generateImageService.sendingGenerateImageRequest(generateImageRequest));
    }

    // 생성된 이미지를 캐릭터에 반영하는 엔드포인트
//    @PostMapping("/")
}
