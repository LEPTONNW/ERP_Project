package com.example.demo.controller;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import com.example.demo.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("isAuthenticated()")
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
    public String list(@ModelAttribute PageRequestDTO pageRequestDTO, Model model) {
        // 페이지 번호가 1 미만일 경우 1로 설정
        if (pageRequestDTO.getPage() < 1) {
            pageRequestDTO.setPage(1);
        }

        // 게시물 목록 조회
        PageResponesDTO<BoardDTO> boardDTOPageResponesDTO = boardService.list(pageRequestDTO);

        // 게시물 리스트가 비어있으면 빈 리스트 설정
        if (boardDTOPageResponesDTO.getDtoList() == null || boardDTOPageResponesDTO.getDtoList().isEmpty()) {
            boardDTOPageResponesDTO.setDtoList(Collections.emptyList());
        } else {
            // 게시물 제목 및 내용 길이 제한
            boardDTOPageResponesDTO.getDtoList().forEach(boardDTO -> {
                if (boardDTO.getTitle() != null && boardDTO.getTitle().length() > 10) {
                    boardDTO.setTitle(boardDTO.getTitle().substring(0, 10) + "...");
                }
                if (boardDTO.getContent() != null && boardDTO.getContent().length() > 10) {
                    boardDTO.setContent(boardDTO.getContent().substring(0, 10) + "...");
                }
                log.info(boardDTO);
            });
        }

        // 현재 페이지 및 총 페이지 수
        int currentPage = pageRequestDTO.getPage();
        int totalPages = boardDTOPageResponesDTO.getTotalPages();

        // 모델에 게시물 리스트와 첫/마지막 페이지 정보를 추가
        model.addAttribute("boardDTOPageResponesDTO", boardDTOPageResponesDTO);
        model.addAttribute("firstPage", 1); // 첫 페이지
        model.addAttribute("lastPage", totalPages); // 마지막 페이지

        return "board/list";
    }


    @GetMapping("/read")
    public String read(Model model, Long bno) {
        // 게시글 번호를 통해 상세 정보를 가져옴
        BoardDTO boardDTO = boardService.read(bno);

        // 모델에 담아 뷰로 전달
        model.addAttribute("boardDTO", boardDTO);

        // 상세 페이지 뷰로 이동
        return "board/boardread";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String modify(Model model, @RequestParam Long bno) {
        BoardDTO boardDTO = boardService.read(bno);
        model.addAttribute("boardDTO", boardDTO);
        return "board/modify"; // 수정 화면으로 이동
    }

    @PreAuthorize("isAuthenticated()")
    // 수정된 내용 저장
    @PostMapping("/modify")
    public String modifyPro(@ModelAttribute BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
       Long num = boardService.modify(boardDTO); // 수정된 내용을 서비스에서 처리
        redirectAttributes.addFlashAttribute("message", num+ "번 글이 수정이 완료되었습니다.");
        return "redirect:/board/list"; // 목록 페이지로 리다이렉트
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam Long bno) {
        boardService.delete(bno);
        return "redirect:/board/list"; // 삭제 후 목록 페이지로 리다이렉트
    }

}