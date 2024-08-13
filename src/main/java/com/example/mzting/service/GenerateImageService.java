package com.example.mzting.service;

import com.example.mzting.dto.GenerateImageDTO;
import com.example.mzting.entity.ImageTag;
import com.example.mzting.repository.ImageTagRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public GenerateImageService(ImageTagRepository imageTagRepository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.imageTagRepository = imageTagRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<GenerateImageDTO.ImageTagResponse> getImageTags() {
        return imageTagRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private GenerateImageDTO.ImageTagResponse convertToDto(ImageTag imageTag) {
        GenerateImageDTO.ImageTagResponse dto = new GenerateImageDTO.ImageTagResponse();
        dto.setId(imageTag.getId());
        dto.setKorName(imageTag.getKorName());
        dto.setCategory(imageTag.getCategory());

        return dto;
    }

    public GenerateImageDTO.GenerateImageResponse sendingGenerateImageRequest(GenerateImageDTO.GenerateImageRequest generateImageRequest) {
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
                System.out.println("Raw response from Flask: " + response.getBody());
                GenerateImageDTO.FlaskGenerateImageResponse flaskResponse = objectMapper.readValue(response.getBody(), GenerateImageDTO.FlaskGenerateImageResponse.class);
                System.out.println("Deserialized response: " + flaskResponse);
                System.out.println("Image URL: " + (flaskResponse != null ? flaskResponse.getImageUrl() : "null"));

                GenerateImageDTO.GenerateImageResponse result = new GenerateImageDTO.GenerateImageResponse();
                result.setImageUrl(flaskResponse.getImageUrl());
                System.out.println("Final result: " + result);
                return result;
            } catch (Exception e) {
                System.out.println("Error parsing response: " + e.getMessage());
                throw new RuntimeException("Failed to parse image generation response", e);
            }
        } else {
            throw new RuntimeException("Failed to generate image: " + response.getStatusCode());
        }
    }
}
