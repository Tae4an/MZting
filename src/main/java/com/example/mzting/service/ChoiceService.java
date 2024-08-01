package com.example.mzting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ChoiceService 클래스
 * 상황에 대한 무작위 선택지를 제공하는 서비스 클래스
 */
@Service
public class ChoiceService {

    // JDBC 템플릿
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 주어진 상황 ID에 대한 무작위 선택지를 반환하는 메서드
     *
     * @param situationId 상황 ID
     * @param count 선택지 개수
     * @return 무작위 선택지 목록
     */
    public List<String> getRandomChoices(int situationId, int count) {
        String sql = "SELECT description FROM choice WHERE situation_id = ? ORDER BY RAND() LIMIT ?";
        return jdbcTemplate.queryForList(sql, String.class, situationId, count);
    }
}
