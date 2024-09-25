package com.example.demo.controller;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.service.annonService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

        //BoardDTO.setWriter(username);
        //model.addAttribute( , );


        return "annon/main";
    }

    @GetMapping("/create")
    public String create() {
        return null;
    }

}
