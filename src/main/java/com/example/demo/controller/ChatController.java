package com.example.demo.controller;

import com.example.demo.dto.ChatMessageDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    private List<ChatMessageDTO> messages = new ArrayList<>();

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/chat")
    public String chat(Model model) {
        model.addAttribute("messages", messages);
        model.addAttribute("chatMessage", new ChatMessageDTO());
        return "chat";
    }

    @PostMapping("/chat/send")
    public String sendMessage(ChatMessageDTO chatMessage) {
        messages.add(chatMessage);
        return "redirect:/chat";
    }
}
