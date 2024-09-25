package com.example.demo.controller;


import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import com.example.demo.entity.Board;
import com.example.demo.repository.search.BoardSearch;
import com.example.demo.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")

public class BoardController {
    //서비스들 가져오기 그리고 필요하다면 user등등 다
    private  final BoardService boardService;
    // 댓글도
    //이미지도

    @GetMapping("/register")
    public  void register(BoardDTO boardDTO){
        //html에서 object를 사용하기 위해서 thymeleaf
        log.info("등록get 진입");



    }

    @PostMapping("/register")
    public  String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, Model model
    ){  //파라미터 리다이렉트 쓸때 추가 : RedirectAttributes redirectAttributes

        log.info("파라미터로 입력된 : " +boardDTO);

        if(bindingResult.hasErrors()){ //유효성검사간 에러가 있니?

            log.info("에러");
            log.info("에러");
            log.info("에러");
            log.info("에러");
            log.info(bindingResult.getAllErrors()); //유효성검사에 대한 결과
            //redirectAttributes.addAttribute("errors", bindingResult.getAllErrors());
//            model.addAttribute("boardDTO" , boardDTO);
            return "board/register";

        }



        boardService.register(boardDTO);

        return "redirect:/board/list";
    }

    @GetMapping("/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {
        log.info("pageR : " + pageRequestDTO);
//
//        List<BoardDTO> boardList = boardService.selectAll();
//
//        model.addAttribute("list", boardList);

        PageResponesDTO<BoardDTO>  boardDTOPageResponesDTO
                =  boardService.list(pageRequestDTO);

        boardDTOPageResponesDTO.getDtoList().forEach(boardDTO -> log.info(boardDTO));

        model.addAttribute("boardDTOPageResponesDTO", boardDTOPageResponesDTO);

        return  null;
    }








}
