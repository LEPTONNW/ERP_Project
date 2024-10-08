package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDTO {
    private String sender;
    private String content;
    private MessageType type;


    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
