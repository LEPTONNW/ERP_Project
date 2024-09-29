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

import java.io.Console;
import java.util.Collections;

@Controller
@Log4j2
@RequiredArgsConstructor

@RequestMapping("/material")
public class MaterialController {
    private final MaterialService materialService;

    @GetMapping("/register")
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
        if (!materialDTO.getImgFile().isEmpty()) {
            String imgUrl = materialService.uploadFile(materialDTO.getImgFile());
            materialDTO.setImgUrl(imgUrl);
        }


        materialService.register(materialDTO);
        return "redirect:/material/list";
    }
    @GetMapping("/list")
    public  String list(Model model, PageRequestDTO pageRequestDTO){
        PageResponesDTO<MaterialDTO> materialDTOPageResponesDTO = materialService.list(pageRequestDTO);
        if (materialDTOPageResponesDTO.getDtoList() == null) {
            materialDTOPageResponesDTO.setDtoList(Collections.emptyList());
        }
        model.addAttribute("materialDTOPageResponesDTO",materialDTOPageResponesDTO);
        return "material/list";
    }
    @GetMapping("/read")
    public String readOne(Long num, Model model){
        model.addAttribute("materialDTO",materialService.read(num));
        return "material/read";
    }
    @GetMapping("/modify")
    public String modify(Long num,Model model){
        model.addAttribute("materialDTO",materialService.read(num));
        return "material/modify";
    }
    @PostMapping("/modify")
    public String modify(@Valid MaterialDTO materialDTO, BindingResult bindingResult)
    {

        if (materialDTO.getNum() == null) {
            log.info("왔어? 포스트긴해 근디 ID값없어서 왔다?");
            bindingResult.rejectValue("num", "error.materialDTO", "ID 값이 필요합니다.");
            return "material/modify";
        }

        log.info(materialDTO);
        if(bindingResult.hasErrors()){
            log.info("에러");
            log.info(bindingResult.getAllErrors());
            return "material/modify";
        }
        materialService.update(materialDTO);
        return "redirect:/material/list";
    }
    @GetMapping("/del")
    public String delete(Long num, Model model){

        Long number = materialService.delete(num);

        if (number == null) {
            //실패했습니다. 라는
            log.info("혹시 컨트롤러까진 오셨나요");
            return "redirect:/material/list";

        } else {

            // number  번이 삭제되었습니다.
            return "redirect:/material/list";
        }

    }
}