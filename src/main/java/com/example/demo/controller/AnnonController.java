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
    public String main(Model model, PageRequestDTO pageRequestDTO) {

        // 페이지 번호가 음수인 경우 0으로 설정
        if (pageRequestDTO.getPage() < 0) {
            pageRequestDTO.setPage(0);
        }

        // 게시물 목록을 페이징 처리하여 가져옴
        PageResponesDTO<AnnonDTO> annonDTOPageResponesDTO = annonService.main(pageRequestDTO);

        // DTO 리스트가 null인 경우 빈 리스트로 설정
        if (annonDTOPageResponesDTO.getDtoList() == null) {
            annonDTOPageResponesDTO.setDtoList(Collections.emptyList());
        } else {
            // 제목과 내용을 간략히 표시
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

        // 첫 페이지 여부 설정
        annonDTOPageResponesDTO.setFirst(pageRequestDTO.getPage() == 0);

        // 마지막 페이지 여부 설정
        annonDTOPageResponesDTO.setLast(pageRequestDTO.getPage() + 1 >= annonDTOPageResponesDTO.getTotalPages());

        // 모델에 페이징된 응답 추가
        model.addAttribute("annonDTOPageResponesDTO", annonDTOPageResponesDTO);

        // 뷰 이름 반환
        return "annon/main";
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
