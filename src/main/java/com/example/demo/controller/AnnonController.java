package com.example.demo.controller;

import com.example.demo.dto.AnnonDTO;
import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import com.example.demo.entity.Annon;
import com.example.demo.service.AnnonService;
import com.example.demo.service.AnnonServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

@Controller
@RequestMapping("/annon")
@RequiredArgsConstructor
@Log4j2
public class AnnonController {

    private final AnnonService annonService;

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/create")
    public String create(AnnonDTO annonDTO){
        return "annon/create";
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/create")
    public  String Create(@Valid AnnonDTO annonDTO, BindingResult bindingResult, Model model) {
        log.info("파라미터로 입력된 : " + annonDTO);

        if(bindingResult.hasErrors()){
            log.info(bindingResult.getAllErrors());
            return "annon/create";
        }
        annonService.create(annonDTO);
        return "redirect:/annon/main";
    }



    @GetMapping("/main")
    public String main(@ModelAttribute PageRequestDTO pageRequestDTO, Model model) {
        // 페이지 번호가 1 미만일 경우 1로 설정
        if (pageRequestDTO.getPage() < 1) {
            pageRequestDTO.setPage(1);
        }

        // 게시물 목록 조회
        PageResponesDTO<AnnonDTO> annonDTOPageResponesDTO = annonService.main(pageRequestDTO);

        // 게시물 리스트가 비어있으면 빈 리스트 설정
        if (annonDTOPageResponesDTO.getDtoList() == null || annonDTOPageResponesDTO.getDtoList().isEmpty()) {
            annonDTOPageResponesDTO.setDtoList(Collections.emptyList());
        } else {
            // 게시물 제목 및 내용 길이 제한
            annonDTOPageResponesDTO.getDtoList().forEach(annonDTO -> {
                if (annonDTO.getTitle() != null && annonDTO.getTitle().length() > 10) {
                    annonDTO.setTitle(annonDTO.getTitle().substring(0, 10) + "...");
                }
                if (annonDTO.getContent() != null && annonDTO.getContent().length() > 10) {
                    annonDTO.setContent(annonDTO.getContent().substring(0, 10) + "...");
                }
                log.info(annonDTO);
            });
        }

        // 현재 페이지 및 총 페이지 수
        int currentPage = pageRequestDTO.getPage();
        int totalPages = annonDTOPageResponesDTO.getTotalPages();

        // 모델에 게시물 리스트와 첫/마지막 페이지 정보를 추가
        model.addAttribute("annonDTOPageResponesDTO", annonDTOPageResponesDTO); // 모델에 annonDTO 추가
        model.addAttribute("firstPage", 1); // 첫 페이지
        model.addAttribute("lastPage", totalPages); // 마지막 페이지

        return "annon/main"; // 반환되는 뷰 이름
    }
    @GetMapping("/load")
    public String load(Model model, Long bno) {
        AnnonDTO annonDTO = annonService.load(bno);

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
    @GetMapping("/delete")
    public String delete(Model model, @RequestParam Long bno) {
        AnnonDTO annonDTO = annonService.load(bno);
        model.addAttribute("annonDTO", annonDTO);
        return "annon/delete";
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/delete")
    public String deletePro(@RequestParam Long bno) {
        AnnonDTO annonDTO = new AnnonDTO();
        annonDTO.setBno(bno);

        annonService.delete(annonDTO);

        return "redirect:/annon/main";
    }




}
