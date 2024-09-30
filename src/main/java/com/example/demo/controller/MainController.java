package com.example.demo.controller;

import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.UsersEntity;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

import java.io.IOException;


import java.io.IOException;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

    private final UserService userService;


    //메인페이지
    @GetMapping("/")
    public String home() {
        return "main";
    }

    @GetMapping("/main")
    public String home2() {
        return "main";
    }

    //로그인 페이지 전환
    //@GetMapping("/login")
    //public String login() {
    //    return "login";
    //}

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //현재 인증된 사용자의 정보를 가져옴

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            // 이미 로그인된 상태이면 메인 페이지로 리다이렉트
            response.sendRedirect("/");
            return null;
        }
        return "login";
    }

    //권한 필요 페이지
    @GetMapping("/acc-denied")
    public String acc() {return "acc-denied"; }


    //관리자 페이지
    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/adminPage")
    public String adminPage (Model model) {
        try{
            List<UsersDTO> usersDTOList = userService.getAllUser(); //모든 정보 가져옴
            model.addAttribute("userDTOList", usersDTOList); //모델로 바인딩
            return "adminPage";
        }
        catch (Exception e) {
            model.addAttribute("err" , "ERROR : 현재 유저정보가 없습니다.");
            return "adminPage";
        }

    }

}
