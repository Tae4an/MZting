package com.example.mzting.controller;

import com.example.mzting.DTO.Answer;
import com.example.mzting.entity.MBTIQuestion;
import com.example.mzting.repository.MBTIQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/question")
public class MBTIQuestionController {
    @Autowired
    private MBTIQuestionRepository questionRepository;

    @GetMapping
    public List<MBTIQuestion> getQuestions() {
        return questionRepository.findAll();
    }

    @PostMapping("/submit")
    public String submitAnswers(@RequestBody List<Answer> answers) {
        // MBTI 판별 로직
        String mbti = determineMBTI(answers);
        return mbti;
    }

    private String determineMBTI(List<Answer> answers) {
        int E = 0, I = 0, S = 0, N = 0, T = 0, F = 0, J = 0, P = 0;
        for (Answer answer : answers) {
            switch (answer.getQuestionId().intValue()) {
                case 1:
                case 2:
                case 3:
                    if (answer.getOption().equals("A")) I++; else E++;
                    break;
                case 4:
                case 5:
                case 6:
                    if (answer.getOption().equals("A")) N++; else S++;
                    break;
                case 7:
                case 8:
                case 9:
                    if (answer.getOption().equals("A")) T++; else F++;
                    break;
                case 10:
                case 11:
                case 12:
                    if (answer.getOption().equals("A")) J++; else P++;
                    break;
            }
        }
        return "" + (E > I ? "E" : "I") + (S > N ? "S" : "N") + (T > F ? "T" : "F") + (J > P ? "J" : "P");
    }
}