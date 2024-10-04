package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.repository.BoardRepository;
import com.example.demo.service.BoardService;
import com.example.demo.service.ReplyService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")

public class BoardController {
    //서비스들 가져오기 그리고 필요하다면 user등등 다
    private  final BoardService boardService;
    private final BoardRepository boardRepository;

    @Autowired
    private final UserService userService;

    private final ReplyService replyService;

    @GetMapping("/register")
    public void register(BoardDTO boardDTO,
                         Principal principal,
                         Model model){
        // 유저정보를 가져오기
        UsersDTO usersDTO =  userService.getUser(principal.getName());

        // 유저의 정보를 boardDTO에 설정
        boardDTO.setWriter(usersDTO.getName());

        // 모델에 전달
        model.addAttribute("boardDTO", boardDTO);

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register")
    public String register(@Valid BoardDTO boardDTO, BindingResult bindingResult, Principal principal
    ){

        //유효성 검사간 에러가 있는지 확인
        if(bindingResult.hasErrors()){
            log.info("값이 비워있음",bindingResult.getAllErrors());
            return "board/register";
        }
        // 유저정보를 가져오기
        UsersDTO usersDTO =  userService.getUser(principal.getName());

        // 유저의 정보를 boardDTO에 설정
        boardDTO.setWriter(usersDTO.getName());

        // boardService의 register 메서드를 호출하여 게시물을 등록
        boardService.register(boardDTO, principal);

        return "redirect:/board/list";
    }

    @GetMapping("/list")
    public String boardList(@ModelAttribute PageRequestDTO pageRequestDTO, @ModelAttribute AdminSearchDTO adminSearchDTO,
                       Model model, @RequestParam(value = "page", defaultValue = "1") int page) {

        //초기화
        List<BoardDTO> list = new ArrayList<>();
        ///////////////////////////
        // 검색 처리구간 ////////////
        ///////////////////////////
        ///////////////////////////
        model.addAttribute("seDTO", adminSearchDTO); //검색폼 바인딩
        try {
            //제목으로 검색
            if(adminSearchDTO.getType().equals("t")) {
                if(adminSearchDTO.getKeyword() != null || !adminSearchDTO.getKeyword().isEmpty()) {
                    list = new ArrayList<>(boardService.titlelike("%" + adminSearchDTO.getKeyword() + "%"));
                }
            }
            //내용으로 검색
            else if(adminSearchDTO.getType().equals("c")) {
                if(adminSearchDTO.getKeyword() != null || !adminSearchDTO.getKeyword().isEmpty()) {
                    list = new ArrayList<>(boardService.contentlike("%" + adminSearchDTO.getKeyword() + "%"));
                }
            }
            //작성자로 검색
            else if(adminSearchDTO.getType().equals("w")) {
                if(adminSearchDTO.getKeyword() != null || !adminSearchDTO.getKeyword().isEmpty()) {
                    list = new ArrayList<>(boardService.writerlike("%" + adminSearchDTO.getKeyword() + "%"));
                }
            }
            //아무값도 없으면 전체검색
            else {
                PageResponesDTO<BoardDTO> boardDTOPageResponesDTO = boardService.list(pageRequestDTO);
                list = new ArrayList<>(boardService.boardDTOList()); //모든 보드 레코드 가져옴
            }
        }
        //아무값도 없으면 전체검색
        catch (Exception e) {
            PageResponesDTO<BoardDTO> boardDTOPageResponesDTO = boardService.list(pageRequestDTO);
            list = new ArrayList<>(boardService.boardDTOList()); //모든 보드 레코드 가져옴
            model.addAttribute("err" , "ERROR : 찾은 정보가 없습니다.");
            //return "annon/main";
        }


        // 페이징 처리 구간
        List<BoardDTO[]> paginatedUserList = getPaginatedUserList(list, 10);
        try {
            // 현재 페이지에 해당하는 데이터를 모델로 바인딩
            if (page <= paginatedUserList.size()) {
                model.addAttribute("userDTOList", paginatedUserList.get(page - 1));
            } else {
                model.addAttribute("err", "ERROR: 유효하지 않은 페이지 접근입니다.");
            }

            // 총 페이지 수와 현재 페이지 정보를 모델에 추가
            model.addAttribute("totalPages", paginatedUserList.size());
            model.addAttribute("currentPage", page);

            return "board/list";
        } catch (Exception e) {
            model.addAttribute("err", "ERROR : 유효하지 않은 페이지 접근입니다.");
            return "board/list";
        }
    }



    @GetMapping("/read")
    public String read(Model model,
                       Long bno,
                       Principal principal,
                       ReplyDTO replyDTO) {

        if (principal == null) {
            // 인증되지 않은 사용자의 경우
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        model.addAttribute("logUser", principal.getName());
        UsersDTO uDTO = userService.getUser(principal.getName());
        String usname = uDTO.getName();
        model.addAttribute("logName", usname);


        // 게시글 번호를 통해 상세 정보를 가져옴
        BoardDTO boardDTO = boardService.read(bno);



        // 유저DTO에서 사용자 정보를 가져옴
        UsersDTO usersDTO = userService.getUser(principal.getName());

        // 댓글 작성자 지정
        String writer = principal.getName();
        replyDTO.setRwriter(writer);

        // 댓글 내용이 제대로 들어오는지 확인
        log.info("댓글 작성자: " + replyDTO.getRwriter() + ", 댓글 내용: " + replyDTO.getRcontent());

        // 댓글 서비스 호출
        List<ReplyDTO> replyDTOList = replyService.replyRead(bno);
        if(replyDTOList == null){
            replyDTOList.forEach(replyDTO1 -> log.info("댓글이 존재하지 않습니다.", replyDTO1));
        }

        // 게시글 작성자 설정

        // 모델에 담아 뷰로 전달
        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("replyDTOList", replyDTOList);

        // 상세 페이지 뷰로 이동
        return "board/boardread";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String modify(Model model, @RequestParam Long bno) {
        // bno(게시물 번호)로 조회후 boardDTO에 저장
        BoardDTO boardDTO = boardService.read(bno);

        // boardDTO 모델에 담에 뷰로 전달
        model.addAttribute("boardDTO", boardDTO);
        return "board/modify"; // 수정 화면으로 이동
    }

    @PreAuthorize("isAuthenticated()")
    // 수정된 내용 저장
    @PostMapping("/modify")
    public String modifyPro(@ModelAttribute BoardDTO boardDTO,
                            RedirectAttributes redirectAttributes) {
        // 수정된 내용을 서비스에서 처리
        Long num = boardService.modify(boardDTO);

        // 수정을 하면 수정된 게시물 번호화 함께 메시지 전달
        redirectAttributes.addFlashAttribute("message", num+ "번 글이 수정이 완료되었습니다.");
        return "redirect:/board/list"; // 목록 페이지로 리다이렉트
    }


    @PostMapping("/delete")
    public String deletePost(@RequestParam Long bno) {

        // 삭제한 게시물을 bno번호로 처리
        boardService.delete(bno);
        return "redirect:/board/list"; // 삭제 후 목록 페이지로 리다이렉트
    }

    //페이징처리를 컨트롤러에서 직접 할 것임
    public List<BoardDTO[]> getPaginatedUserList(List<BoardDTO> usersDTOList, int itemsPerPage) {
        int totalRecords = usersDTOList.size();         //UserEntity 가 가진 유저의 총 레코드 수
        int totalPages = totalRecords / itemsPerPage;   //10으로 나눈 몫 || 한 화면에 10개씩 페이지 이동버튼을 보이기위함
        int remainder = totalRecords % itemsPerPage;    //10으로 나눈 나머지

        if (remainder != 0) { //나머지가 0이 아닐 경우 작동
            totalPages += 1; // 총 페이지 수 +1 은 2차원 배열의 크기를 지정하기 위함임 0부터 시작하기 때문
        }

        BoardDTO[][] paginatedArray = new BoardDTO[totalPages][]; //UsesDTO 타입인 2차원 배열 생성
        for (int i = 0; i < totalPages; i++) {
            int start = i * itemsPerPage; //배열 시작점
            int end = Math.min(start + itemsPerPage, totalRecords); //배열 끝점
            paginatedArray[i] = usersDTOList.subList(start, end).toArray(new BoardDTO[0]); //2차원 배열 최대크기 지정 및 값저장
        }

        return Arrays.asList(paginatedArray); //가공된 2차원 배열 정보 리턴
    }
}