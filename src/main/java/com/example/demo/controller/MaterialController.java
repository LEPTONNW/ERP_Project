package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.BimgSerivce;
import com.example.demo.service.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.io.File;

@Controller
@Log4j2
@RequiredArgsConstructor

@RequestMapping("/material")
public class MaterialController {
    private final MaterialService materialService;
    private final BimgSerivce bimgSerivce;

    @GetMapping("/register")
    public  void register(MaterialDTO materialDTO){

    }
    @PostMapping("/register")
    public  String registerPost(@Valid MaterialDTO materialDTO, BindingResult bindingResult, Model model,
                                Principal Principal,  MultipartFile multipartFile){

        log.info("파라미터로 입력된 : " +materialDTO);

        if(bindingResult.hasErrors()){
            log.info("에러");
            log.info(bindingResult.getAllErrors());
            return "material/register";
        }

        if (multipartFile !=null){
            log.info("이름이다" + multipartFile.getOriginalFilename());
            log.info("크기다" + multipartFile.getSize());
        }

        boolean result =  materialService.register(materialDTO, multipartFile);
        if(result == true){
            return "redirect:/material/list";
        }else {
            model.addAttribute("msg","사용된 자재코드");
            return "material/register";
        }

    }
    @GetMapping("/list")
    public  String list(Model model, PageRequestDTO pageRequestDTO){
        PageResponesDTO<MaterialDTO> materialDTOPageResponesDTO = materialService.list(pageRequestDTO);


        if (materialDTOPageResponesDTO.getDtoList() == null) {
            materialDTOPageResponesDTO.setDtoList(Collections.emptyList());
        }

        List<BimgDTO> bimgDTOList =  bimgSerivce.allread();
        for (BimgDTO bimgDTO : bimgDTOList) {
            log.info("BimgDTO: {}", bimgDTO);
        }
        bimgDTOList.forEach(bimgDTO -> log.info(bimgDTO));
        model.addAttribute("materialDTOPageResponesDTO",materialDTOPageResponesDTO);
        model.addAttribute("bimgDTOList",bimgDTOList);
        return "material/list";
    }
    @GetMapping("/read")
    public String readOne(Long num, Model model){
        MaterialDTO materialDTO = materialService.read(num);
        BimgDTO bimgDTO = bimgSerivce.read(num);


        model.addAttribute("materialDTO",materialDTO);
        model.addAttribute("bimgDTO",bimgDTO);

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