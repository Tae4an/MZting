package com.example.mzting.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 좋아요 및 싫어요 개수 정보를 나타내는 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeDislikeCountInfo {

    // 총 좋아요 개수
    private long totalLikeCount;

    // 총 싫어요 개수
    private long totalDislikeCount;
}
