package com.example.demo.controller;


import com.example.demo.dto.ReplyDTO;
import com.example.demo.service.BoardService;
import com.example.demo.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/reply")
@RequiredArgsConstructor
@Log4j2
public class ReplyController {

    private final ReplyService replyService;
    private  final BoardService boardService;



    @GetMapping("/create")
    public String replyCreate(@ModelAttribute ReplyDTO replyDTO ) {

        return "/reply/create";
    }

    @PostMapping("/create")
    public String replyCreatePost(@ModelAttribute ReplyDTO replyDTO,
                                  Principal principal) {

        String writer = principal.getName();
        replyDTO.setRwriter(writer); //댓글작성자 지정;

        replyService.replyCreate(replyDTO, principal.getName());

        return "redirect:/board/read?bno=" + replyDTO.getBno();
    }

    @GetMapping("/modify")
    public String replyModify(@ModelAttribute ReplyDTO replyDTO, Model model) {


        ReplyDTO dto =  replyService.replyOne(replyDTO.getRno());

        model.addAttribute("replyDTO" , dto);

        return "/reply/modify";
    }

    @PostMapping("/modify")
    public String replyModifyPost(@ModelAttribute ReplyDTO replyDTO) {


        Long bno = replyService.readModify(replyDTO);


        return "redirect:/board/read?bno=" + bno;
    }

    @PostMapping("/delete")
    public String replyDelete(@ModelAttribute ReplyDTO replyDTO) {

        replyService.replyDelete(replyDTO);

        return "redirect:/board/read?bno=" +  replyDTO.getBno();
    }

}
