package com.example.mzting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChoiceService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getRandomChoices(int situationId, int count) {
        String sql = "SELECT description FROM choice WHERE situation_id = ? ORDER BY RAND() LIMIT ?";
        return jdbcTemplate.queryForList(sql, String.class, situationId, count);
    }
}