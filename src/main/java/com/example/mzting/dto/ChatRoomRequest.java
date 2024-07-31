package com.example.mzting.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequest {
    private String name;
    private Long userId;
    private Integer profileId;
}
