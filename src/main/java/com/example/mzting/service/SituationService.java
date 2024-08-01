package com.example.mzting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SituationService 클래스
 * 무작위로 장소를 선택하는 서비스 클래스
 */
@Service
public class SituationService {

    // JDBC 템플릿
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 지정된 개수만큼 무작위로 장소를 선택하는 메서드
     *
     * @param count 선택할 장소의 개수
     * @return 무작위로 선택된 장소 목록
     */
    public List<String> getRandomPlaces(int count) {
        String sql = "SELECT place FROM situation WHERE situation_id <= 6 ORDER BY RAND() LIMIT ?";
        return jdbcTemplate.queryForList(sql, String.class, count);
    }
}
