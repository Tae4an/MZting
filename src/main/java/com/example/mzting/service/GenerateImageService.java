package com.example.mzting.service;

import com.example.mzting.entity.ImageTag;
import com.example.mzting.repository.ImageTagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateImageService {
    private final ImageTagRepository imageTagRepository;

    public GenerateImageService(ImageTagRepository imageTagRepository) {
        this.imageTagRepository = imageTagRepository;
    }

    public List<ImageTag> getImageTags() {
        return imageTagRepository.findAll();
    }
}
