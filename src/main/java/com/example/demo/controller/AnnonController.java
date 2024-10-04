package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.AnnonService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@RequestMapping("/annon")
@RequiredArgsConstructor
@Log4j2
public class AnnonController {

    private final AnnonService annonService;

    private final UserService userService;

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/create")
    public void create(AnnonDTO annonDTO, Principal principal, Model model){
        UsersDTO usersDTO = userService.getUser(principal.getName());

        log.info(usersDTO);
        annonDTO.setWriter(usersDTO.getName());

        model.addAttribute("annonDTO", annonDTO);
        log.info("get 공지 진입ㅇㅎ");
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/create")
    public String Create(@Valid AnnonDTO annonDTO, BindingResult bindingResult, Model model, Principal principal) {
        //log.info("파라미터로 입력된 : " + annonDTO);
        //log.info("파라미터로 입력된 : " + principal.getName());

        if(bindingResult.hasErrors()){
            log.info(bindingResult.getAllErrors());
            return "annon/create";
        }

        annonService.create(annonDTO, principal);


        return "redirect:/annon/main";
    }



    @GetMapping("/main")
    public String main(@ModelAttribute PageRequestDTO pageRequestDTO, @ModelAttribute AdminSearchDTO adminSearchDTO,
                       Model model, @RequestParam(value = "page", defaultValue = "1") int page) {


        //초기화
        List<AnnonDTO> list = new ArrayList<>();

        ///////////////////////////
        // 검색 처리구간 ////////////
        ///////////////////////////
        ///////////////////////////
        model.addAttribute("seDTO", adminSearchDTO); //검색폼 바인딩
        try {
            //제목으로 검색
            if(adminSearchDTO.getType().equals("t")) {
                if(adminSearchDTO.getKeyword() != null || !adminSearchDTO.getKeyword().isEmpty()) {
                    list = new ArrayList<>(annonService.titlelike("%" + adminSearchDTO.getKeyword() + "%"));
                }
            }
            //내용으로 검색
            else if(adminSearchDTO.getType().equals("c")) {
                if(adminSearchDTO.getKeyword() != null || !adminSearchDTO.getKeyword().isEmpty()) {
                    list = new ArrayList<>(annonService.contentlike("%" + adminSearchDTO.getKeyword() + "%"));
                }
            }
            //작성자로 검색
            else if(adminSearchDTO.getType().equals("w")) {
                if(adminSearchDTO.getKeyword() != null || !adminSearchDTO.getKeyword().isEmpty()) {
                    list = new ArrayList<>(annonService.writerlike("%" + adminSearchDTO.getKeyword() + "%"));
                }
            }
            //아무값도 없으면 전체검색
            else {
                PageResponesDTO<AnnonDTO> boardDTOPageResponesDTO = annonService.main(pageRequestDTO);
                list = new ArrayList<>(annonService.getallAnnon());
            }
        }
        catch (Exception e) {
            model.addAttribute("err" , "ERROR : 찾은 정보가 없습니다.");
            return "annon/main";
        }


        PageResponesDTO<AnnonDTO> boardDTOPageResponesDTO = annonService.main(pageRequestDTO);
        list = new ArrayList<>(annonService.getallAnnon());

        log.info(list.toArray());
        log.info(list.toArray());
        log.info(list.toArray());
        ///////////////////////////
        //페이징 처리구간 ////////////
        ///////////////////////////
        ///////////////////////////
        List<AnnonDTO[]> paginatedUserList = getPaginatedUserList(list, 10);
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

            return "annon/main";
        } catch (Exception e) {
            model.addAttribute("err", "ERROR : 유효하지 않은 페이지 접근입니다.");
            return "annon/main";
        }
    }


    @GetMapping("/load")
    public String load(Model model, Long bno, Principal principal) {

        // 공지사항 번호를 통해 상세 정보를 가져옴
        AnnonDTO annonDTO = annonService.load(bno);

        if (principal == null) {
            // 인증되지 않은 사용자의 경우
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        // 유저DTO에서 사용자 정보를 가져옴
        UsersDTO usersDTO = userService.getUser(principal.getName());

        annonDTO.setWriter(usersDTO.getName());

        model.addAttribute("annonDTO", annonDTO);

        return "annon/load";
    }


    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/alter")
    public String alter(Model model, @RequestParam Long bno) {
        AnnonDTO annonDTO = annonService.load(bno);
        model.addAttribute("annonDTO", annonDTO);
        return "annon/alter"; // 수정 화면으로 이동
    }

    // 수정된 내용 저장
    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/alter")
    public String alterPro(@ModelAttribute AnnonDTO annonDTO, RedirectAttributes redirectAttributes) {
        Long bno = annonService.alter(annonDTO); // 수정된 내용을 서비스에서 처리
        redirectAttributes.addFlashAttribute("message", bno + "번 글이 수정이 완료되었습니다.");
        return "redirect:/annon/main"; // 목록 페이지로 리다이렉트
    }


    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/delete")
    public String deletePost(@RequestParam Long bno) {
        //
        annonService.delete(bno);
        return "redirect:/board/list"; // 삭제 후 목록 페이지로 리다이렉트
    }

    //페이징처리를 컨트롤러에서 직접 할 것임
    public List<AnnonDTO[]> getPaginatedUserList(List<AnnonDTO> usersDTOList, int itemsPerPage) {
        int totalRecords = usersDTOList.size();         //UserEntity 가 가진 유저의 총 레코드 수
        int totalPages = totalRecords / itemsPerPage;   //10으로 나눈 몫 || 한 화면에 10개씩 페이지 이동버튼을 보이기위함
        int remainder = totalRecords % itemsPerPage;    //10으로 나눈 나머지

        if (remainder != 0) { //나머지가 0이 아닐 경우 작동
            totalPages += 1; // 총 페이지 수 +1 은 2차원 배열의 크기를 지정하기 위함임 0부터 시작하기 때문
        }

        AnnonDTO[][] paginatedArray = new AnnonDTO[totalPages][]; //UsesDTO 타입인 2차원 배열 생성
        for (int i = 0; i < totalPages; i++) {
            int start = i * itemsPerPage; //배열 시작점
            int end = Math.min(start + itemsPerPage, totalRecords); //배열 끝점
            paginatedArray[i] = usersDTOList.subList(start, end).toArray(new AnnonDTO[0]); //2차원 배열 최대크기 지정 및 값저장
        }

        return Arrays.asList(paginatedArray); //가공된 2차원 배열 정보 리턴
    }

}
