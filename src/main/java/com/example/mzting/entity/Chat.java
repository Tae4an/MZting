package com.example.mzting.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_temp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long chatRoomId;

    @Column(nullable = false)
    private Boolean isBot;

    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "json")
    private String botInfo;

    @Column(nullable = false)
    private Integer mission;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime sendAt;
}
