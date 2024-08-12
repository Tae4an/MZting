package com.example.mzting.controller;


import com.example.mzting.dto.CommentDTO;
import com.example.mzting.entity.ImageTag;
import com.example.mzting.service.GenerateImageService;
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

    @GetMapping("/tag")
    public ResponseEntity<List<ImageTag>> getTag() {
        List<ImageTag> imageTags = generateImageService.getImageTags();
        return ResponseEntity.ok(imageTags);
    }
}
