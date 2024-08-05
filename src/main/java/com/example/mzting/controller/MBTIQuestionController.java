package com.example.mzting.controller;

import com.example.mzting.dto.Answer;
import com.example.mzting.entity.MBTIQuestion;
import com.example.mzting.repository.MBTIQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MBTI 질문 관련 요청을 처리하는 컨트롤러 클래스
 * 질문 조회 및 답변 제출과 관련된 API 엔드포인트를 정의
 */
@RestController
@RequestMapping("/api/question")
public class MBTIQuestionController {

    // MBTI 질문 저장소
    private final MBTIQuestionRepository mbtiQuestionRepository;

    public MBTIQuestionController(MBTIQuestionRepository mbtiQuestionRepository) {
        this.mbtiQuestionRepository = mbtiQuestionRepository;
    }

    /**
     * 모든 MBTI 질문을 조회하는 엔드포인트
     *
     * @return 모든 MBTI 질문 목록
     */
    @GetMapping
    public List<MBTIQuestion> getQuestions() {
        return mbtiQuestionRepository.findAll();
    }

    /**
     * MBTI 답변을 제출하는 엔드포인트
     * 주어진 답변 리스트를 바탕으로 MBTI 유형을 판별하고 반환
     *
     * @param answers 사용자가 제출한 답변 리스트
     * @return 판별된 MBTI 유형 문자열
     */
    @PostMapping("/submit")
    public String submitAnswers(@RequestBody List<Answer> answers) {
        // MBTI 판별 로직
        return determineMBTI(answers);
    }

    /**
     * 주어진 답변 리스트를 바탕으로 MBTI 유형을 판별하는 메서드
     *
     * @param answers 사용자가 제출한 답변 리스트
     * @return 판별된 MBTI 유형 문자열
     */
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
