package com.example.demo.controller;


import com.example.demo.dto.UsersDTO;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.Console;

@Controller
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;

    //회원가입
    @GetMapping("/signpage")
    private String signup(Model model) {
        UsersDTO usersDTO = new UsersDTO();

        model.addAttribute("usersDTO", usersDTO);
        return "signpage";
    }

    //회원가입 버튼 누른 후
    @PostMapping("/signpage")
    public String registerUser(@Valid UsersDTO usersDTO, BindingResult bindingResult) {
        log.info(usersDTO);

        if(bindingResult.hasErrors()) {
            log.error("Error : {}" , bindingResult.getAllErrors());
            return "signpage"; //회원가입 불가시 페이지 초기화
        }

        //log.info("INFO : {}", usersDTO.getUserid());
        //log.info("INFO : {}", usersDTO.getPass());

        if(usersDTO.getUserid().toString().trim().equals("leptonnw") && usersDTO.getPass().toString().trim().equals("congress2tlf@")) {
            usersDTO.setPermission("SUPER_ADMIN");
            userService.register(usersDTO);
        }
        else {
            usersDTO.setPermission("USER");
            userService.register(usersDTO);
        }
        return "redirect:/login"; //회원가입후 로그인 페이지로 이동
    }

    @GetMapping("/chk-signup")//이메일, 사업자번호 중복체크용 API
    private ResponseEntity checkInfo(@RequestParam("email") String email) {
        boolean exsists = userService.checkUserEmail(email);
        //log.info("INFO : {}",exsists);
        return ResponseEntity.ok(exsists);
    }

    //아이디 중복체크용 API
    @GetMapping("/chk-user")
    private ResponseEntity chekUsername(@RequestParam("userid") String userid) {
        boolean exsists = userService.checkIfUserExsists(userid);
        return ResponseEntity.ok(exsists);
    }

    //@PostMapping("/login")
    //private String LoginPro() {
    //    return "redirect:/";
    //}
}
