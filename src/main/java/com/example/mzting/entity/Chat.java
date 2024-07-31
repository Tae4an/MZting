package com.example.mzting.entity;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "situation_id")
    private Situation situation;

    private String chatName;

    @Enumerated(EnumType.STRING)
    private SenderType sender;

    private String chatText;

    private Timestamp timestamp;
}
enum SenderType {
    USER, BOT
}