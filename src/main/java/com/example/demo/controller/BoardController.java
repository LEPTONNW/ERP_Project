package com.example.demo.controller;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import com.example.demo.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

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
            log.info(bindingResult.getAllErrors()); //유효성검사에 대한 결과
            return "board/register";
        }
        boardService.register(boardDTO);
        return "redirect:/board/list";
    }

    @GetMapping("/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {

        //페이지에 담을 내용을 boardDTOPageResponesDTO에 리스트로 담아냄
        PageResponesDTO<BoardDTO>  boardDTOPageResponesDTO =  boardService.list(pageRequestDTO);


        // dtoList가 null이면 빈 리스트로 초기화
        if (boardDTOPageResponesDTO.getDtoList() == null) {
            boardDTOPageResponesDTO.setDtoList(Collections.emptyList());
        }

        // content가 10자 이상일 경우 잘라내기 처리
        boardDTOPageResponesDTO.getDtoList().forEach(boardDTO -> {

            // title 자르기
            String title = boardDTO.getTitle();
            if (title != null && title.length() > 10) {
                boardDTO.setTitle(title.substring(0, 10) + "...");
            }

            //content 자르기
            String content = boardDTO.getContent();
            if (content != null && content.length() > 10) {
                boardDTO.setContent(content.substring(0, 10) + "...");
            }
            log.info(boardDTO);  // 로그 확인용
        });

        //모델로 보냄
        model.addAttribute("boardDTOPageResponesDTO", boardDTOPageResponesDTO);

        return  "board/list";
    }

    @GetMapping("/read")
    private String read(Model model, Long bno) {
        // 게시글 번호를 통해 상세 정보를 가져옴
        BoardDTO boardDTO = boardService.read(bno);

        // 모델에 담아 뷰로 전달
        model.addAttribute("boardDTO", boardDTO);

        // 상세 페이지 뷰로 이동
        return "board/boardread";
    }

    @GetMapping("/modify")
    public String modify(Model model, @RequestParam Long bno) {
        BoardDTO boardDTO = boardService.read(bno);
        model.addAttribute("boardDTO", boardDTO);
        return "board/modify"; // 수정 화면으로 이동
    }

    // 수정된 내용 저장
    @PostMapping("/modify")
    public String modifyPro(@ModelAttribute BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
        Long num = boardService.modify(boardDTO); // 수정된 내용을 서비스에서 처리
        redirectAttributes.addFlashAttribute("message", num+ "번 글이 수정이 완료되었습니다.");
        return "redirect:/board/list"; // 목록 페이지로 리다이렉트
    }

}