package com.example.mzting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SituationService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getRandomPlaces(int count) {
        String sql = "SELECT place FROM situation WHERE situation_id <= 6 ORDER BY RAND() LIMIT ?";
        return jdbcTemplate.queryForList(sql, String.class, count);
    }
}