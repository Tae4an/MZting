package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Data
public class Profile {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "profile_id")
        private Integer profileId;

        @Column(name = "character_image", nullable = false)
        private String characterImage;

        @Column(name = "name", nullable = false)
        private String name;

        @Column(name = "mbti", nullable = false, length = 4)
        private String mbti;

        @Column(name = "age", nullable = false)
        private Integer age;

        @Column(name = "height", nullable = false, precision = 5, scale = 2)
        private BigDecimal height;

        @Column(name = "job", nullable = false)
        private String job;

        @Column(name = "description", nullable = false, columnDefinition = "TEXT")
        private String description;

        // Getters and Setters

    // You might want to add a toString() method for debugging purposes
        @Override
        public String toString() {
            return "Profile{" +
                    "profileId=" + profileId +
                    ", name='" + name + '\'' +
                    ", mbti='" + mbti + '\'' +
                    ", age=" + age +
                    ", job='" + job + '\'' +
                    '}';
        }
    }
