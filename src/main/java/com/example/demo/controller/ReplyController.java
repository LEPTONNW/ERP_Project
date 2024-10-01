package com.example.demo.controller;


import com.example.demo.dto.ReplyDTO;
import com.example.demo.service.BoardService;
import com.example.demo.service.ReplyService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/reply")
@RequiredArgsConstructor
@Log4j2
public class ReplyController {

    private final ReplyService replyService;

    private final UserService userService;

    private final BoardService boardService;

    @GetMapping("/create")
    public String replyCreate(@ModelAttribute ReplyDTO replyDTO, @RequestParam Long bno) {

        return "/reply/create";
    }

    @PostMapping("/create")
    public String replyCreatePost(@ModelAttribute ReplyDTO replyDTO, @RequestParam Long bno, Principal principal) {

        String writer = principal.getName();
        replyDTO.setRwriter(writer); //댓글작성자 지정;
        replyDTO.setRtitle(writer); //제목 임시지정

        replyService.replyCreate(replyDTO);

        return "redirect:/board/read?bno=" + bno;
    }
}
