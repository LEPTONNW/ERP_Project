package com.example.demo.controller;

import com.example.demo.dto.ChatMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDTO sendMessage(ChatMessageDTO chatMessageDTO) {
        return chatMessageDTO;
    }

    @MessageMapping("/chat.newUser")
    @SendTo("/topic/public")
    public ChatMessageDTO newUser(ChatMessageDTO chatMessageDTO) {
        chatMessageDTO.setType(ChatMessageDTO.MessageType.JOIN);
        return chatMessageDTO;
    }
}
