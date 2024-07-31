package com.example.mzting.controller;

import com.example.mzting.DTO.ChatResultResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatResultController {

    @PostMapping("/chat/result")
    public ChatResultResponse responseResult() {
        return new ChatResultResponse(1, "으에에", "으에에");
    }
}
