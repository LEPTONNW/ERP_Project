package com.example.demo.controller;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.PageRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/annon")
@RequiredArgsConstructor
@Log4j2
public class AnnonController {

    //private final annonService annonservice;

    @GetMapping("/main")
    public String annonMain(PageRequestDTO pageRequestDTO, Model model, Principal principal) {

        if(principal == null) {
            return "redirect:list";
        }

        String username = principal.getName();

        return "annon/main";
    }




    @GetMapping("/create")
    public String create(BoardDTO boardDTO, Model model, Principal principal) {
        if (principal != null) {
            String name = principal.getName();
            model.addAttribute("writer", "작성자");
        }

        model.addAttribute("message", "공지사항 작성");

        return "annon/create";
    }

    @PostMapping("create/pro")
    public String annonCreatePro() {

        return "redirect:annon/main";
    }

    @GetMapping("/modify")
    public String annonModify() {

        return "annon/modify";
    }



}
