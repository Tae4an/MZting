package com.example.mzting.service;

import com.example.mzting.dto.GenerateImageDTO;
import com.example.mzting.entity.ImageLog;
import com.example.mzting.entity.ImageTag;
import com.example.mzting.entity.UserCustomImage;
import com.example.mzting.repository.ImageLogRepository;
import com.example.mzting.repository.ImageTagRepository;
import com.example.mzting.repository.UserCustomImageRepository;
import com.example.mzting.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenerateImageService {

    private final RestTemplate restTemplate;
    private final ImageTagRepository imageTagRepository;
    private final ImageLogRepository imageLogRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final UserCustomImageRepository userCustomImageRepository;

    public GenerateImageService(ImageTagRepository imageTagRepository,ImageLogRepository imageLogRepository, RestTemplate restTemplate, ObjectMapper objectMapper, UserRepository userRepository, UserCustomImageRepository userCustomImageRepository) {
        this.imageTagRepository = imageTagRepository;
        this.imageLogRepository = imageLogRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.userCustomImageRepository = userCustomImageRepository;
    }

    public List<ImageTag> getImageTags() {
        return imageTagRepository.findAll();
    }

//    private GenerateImageDTO.ImageTagResponse convertToDto(ImageTag imageTag) {
//        GenerateImageDTO.ImageTagResponse dto = new GenerateImageDTO.ImageTagResponse();
//        dto.setId(imageTag.getId());
//        dto.setKorName(imageTag.getKorName());
//        dto.setCategory(imageTag.getCategory());
//
//        return dto;
//    }

    public GenerateImageDTO.GenerateImageResponse sendingGenerateImageRequest(GenerateImageDTO.GenerateImageRequest generateImageRequest, Long uid) {
        String prompt = String.join(",", generateImageRequest.getTags());

        String tempFlaskUrl = "http://localhost:5000";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        GenerateImageDTO.FlaskGenerateImageRequest flaskGenerateImageRequest = new GenerateImageDTO.FlaskGenerateImageRequest(prompt);
        HttpEntity<GenerateImageDTO.FlaskGenerateImageRequest> entity = new HttpEntity<>(flaskGenerateImageRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                tempFlaskUrl + "/gnimage",
                entity,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                GenerateImageDTO.FlaskGenerateImageResponse flaskResponse = objectMapper.readValue(response.getBody(), GenerateImageDTO.FlaskGenerateImageResponse.class);

                GenerateImageDTO.GenerateImageResponse result = new GenerateImageDTO.GenerateImageResponse();
                result.setImageUrl(flaskResponse.getImageUrl());

                ImageLog imageLog = new ImageLog();
                imageLog.setImageUrl(flaskResponse.getImageUrl());
                imageLog.setUserId(uid);

                imageLogRepository.save(imageLog);

                return result;
            } catch (Exception e) {
                System.out.println("Error parsing response: " + e.getMessage());
                throw new RuntimeException("Failed to parse image generation response", e);
            }
        } else {
            throw new RuntimeException("Failed to generate image: " + response.getStatusCode());
        }
    }

    @Transactional
    public void insertDefaultImagesForAllUsers() {
        List<Long> allUserIds = userRepository.findAllUserIds();

        for (Long userId : allUserIds) {
            if (!userCustomImageRepository.existsById(userId)) {
                UserCustomImage defaultImage = new UserCustomImage();
                defaultImage.setId(userId);
                // The constructor will set all default image URLs
                userCustomImageRepository.save(defaultImage);
            }
        }
    }

    @Transactional
    public UserCustomImage setProfileImage(Long uid, int index, String newImageUrl) {
        UserCustomImage userCustomImage = userCustomImageRepository.findById(uid)
                .orElseGet(UserCustomImage::new);

        userCustomImage.setProfileImage(index, newImageUrl);
        return userCustomImageRepository.save(userCustomImage);
    }

    public Page<ImageLog> getImageLogsByUserId(Long uid, Pageable pageable) {
        return imageLogRepository.findAllByUserId(uid, pageable);
    }
}
