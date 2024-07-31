package com.example.mzting.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResultResponse {
    private Integer score;
    private String summaryEval;
    private String summaryFeel;
}
