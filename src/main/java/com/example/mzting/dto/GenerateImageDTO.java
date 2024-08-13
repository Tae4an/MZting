package com.example.mzting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public interface GenerateImageDTO {


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class ImageTagResponse {
        private Long id;
        private String korName;
        private String category;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class GenerateImageRequest {
        private List<String> tags;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class GenerateImageResponse {
        private String imageUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class FlaskGenerateImageRequest {
        private String prompt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class FlaskGenerateImageResponse {
        private String imageUrl;
    }
}
