package com.example.mzting.controller;


import com.example.mzting.dto.GenerateImageDTO;
import com.example.mzting.entity.ImageLog;
import com.example.mzting.service.GenerateImageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Long uid = (Long) request.getAttribute("uid");
        return ResponseEntity.ok(generateImageService.sendingGenerateImageRequest(generateImageRequest, uid));
    }

    // 생성된 이미지를 캐릭터에 반영하는 엔드포인트
    @PostMapping("/apply/{profileId}")
    public ResponseEntity<?> applyImage(@PathVariable int profileId, @RequestBody GenerateImageDTO.ApplyImageRequest applyImageRequest, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("uid");

        generateImageService.setProfileImage(uid, profileId, applyImageRequest.getImageUrl());

        return ResponseEntity.ok("으에에");
    }

    @PostMapping("/log/all")
    public ResponseEntity<GenerateImageDTO.GenerateLogResponse> logAll(
            @RequestBody GenerateImageDTO.ImageLogRequest imageLogRequest,
            HttpServletRequest request
            ) {
        Long uid = (Long) request.getAttribute("uid");

        Pageable pageable = PageRequest.of(imageLogRequest.getPage(), imageLogRequest.getSize());
        Page<ImageLog> imageLogsPage = generateImageService.getImageLogsByUserId(uid, pageable);

        List<ImageLog> contents = imageLogsPage.getContent();
        GenerateImageDTO.PaginationInfo paginationInfo = new GenerateImageDTO.PaginationInfo(
                imageLogsPage.getNumber(),
                imageLogsPage.getTotalPages(),
                imageLogsPage.getTotalElements(),
                imageLogsPage.getSize()
        );

        GenerateImageDTO.GenerateLogResponse response = new GenerateImageDTO.GenerateLogResponse(contents, paginationInfo);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/set")
    public ResponseEntity<?> set(HttpServletRequest request) {
        generateImageService.insertDefaultImagesForAllUsers();
        
        return ResponseEntity.ok("으에에");
    }
}
