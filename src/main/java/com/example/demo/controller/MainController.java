package com.example.demo.controller;


import com.example.demo.dto.UsersDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

    //메인페이지
    @GetMapping("/")
    public String home() {
        return "main";
    }

    //로그인 페이지 전환
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //권한 필요 페이지
    @GetMapping("/acc-denied")
    public String acc() {return "acc-denied"; }


}
