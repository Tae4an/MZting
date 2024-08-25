package com.example.mzting.dto;

import com.example.mzting.entity.ImageLog;
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class ApplyImageRequest {
        private String imageUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class ApplyImageResponse {
        private String imageUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class GenerateLogResponse {
        private List<ImageLog> contents;
        private PaginationInfo paginationInfo;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class PaginationInfo {
        private int currentPage;
        private int totalPages;
        private long totalElements;
        private int size;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class ImageLogRequest {
        private int page = 0;
        private int size = 12;
    }
}
