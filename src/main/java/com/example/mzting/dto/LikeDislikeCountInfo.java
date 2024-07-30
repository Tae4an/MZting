package com.example.mzting.DTO;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeDislikeCountInfo {
    private long totalLikeCount;
    private long totalDislikeCount;
}