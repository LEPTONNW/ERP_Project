package com.example.demo.controller;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.MaterialDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import com.example.demo.service.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/material")
public class MaterialController {
    private final MaterialService materialService;

    @GetMapping("register")
    public  void register(MaterialDTO materialDTO){

    }
    @PostMapping("/register")
    public  String registerPost(@Valid MaterialDTO materialDTO, BindingResult bindingResult, Model model
    ){
        log.info("파라미터로 입력된 : " +materialDTO);
        if(bindingResult.hasErrors()){
            log.info("에러");
            log.info(bindingResult.getAllErrors());
            return "material/register";
        }
        materialService.register(materialDTO);
        return "redirect:/material/list";
    }
    @GetMapping("/list")
    public  String list(Model model, PageRequestDTO pageRequestDTO){
        PageResponesDTO<MaterialDTO> materialDTOPageResponesDTO = materialService.list(pageRequestDTO);
        model.addAttribute("materialDTOPageResponesDTO",materialDTOPageResponesDTO);
        return "material/list";
    }
    @GetMapping("/read")
    public String readOne(Long mno, Model model){
        model.addAttribute("materialDTO",materialService.read(mno));
        return "material/read";
    }
    @GetMapping("/modify")
    public String modify(Long mno,Model model){
        model.addAttribute("materialDTO",materialService.read(mno));
        return "material/modify";
    }
}
