package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.MaterialEntity;
import com.example.demo.repository.BimgRepository;
import com.example.demo.service.BimgSerivce;
import com.example.demo.service.MaterialService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.security.Principal;
import java.util.ArrayList;
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
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/register")
    public  void register(MaterialDTO materialDTO){

    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register")
    public  String registerPost(@Valid MaterialDTO materialDTO, BindingResult bindingResult, Model model,
                                Principal principal,  MultipartFile multipartFile){

        UsersDTO usDto = userService.getUser(principal.getName());


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

        boolean result =  materialService.register(materialDTO, multipartFile, usDto.getUserid());
        if(result == true){
            return "redirect:/material/list";
        }else {
            model.addAttribute("msg","사용된 자재코드");
            return "material/register";
        }

    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public  String list(Model model, PageRequestDTO pageRequestDTO, Principal principal){
        //세션 유저정보 가져옴
        UsersDTO usDTO = userService.getUser(principal.getName());

        //모든 자재정보 가져옴
        PageResponesDTO<MaterialDTO> materialDTOPageResponesDTO = materialService.list(pageRequestDTO);
        //모든 자재정보의 리스트를 리스트에 저장
        List<MaterialDTO> newlist = materialDTOPageResponesDTO.getDtoList();
        //materialDTOPageResponesDTO 의 DtoList를 비워줌
        //materialDTOPageResponesDTO.setDtoList(Collections.emptyList());
        //materialDTOPageResponesDTO.setDtoList(new ArrayList<>());

        //초기화
        //List<MaterialDTO> filterList = new ArrayList<>();

        //자신의 사업자번호로 되어있는것만 리스트에 저장하여 데이터를 가공한다.
        //newlist.forEach(materialDTO -> {
        //    if(materialDTO.getMno().getMno().equals(usDTO.getMno())) {
        //        filterList.add(materialDTO);
        //    }
        //});

        //가공된 값 저장
        //materialDTOPageResponesDTO.setDtoList(filterList);


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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/read")
    public String readOne(Long num, Model model){
        MaterialDTO materialDTO = materialService.read(num);
        BimgDTO bimgDTO = bimgSerivce.read(num);


        model.addAttribute("materialDTO",materialDTO);
        model.addAttribute("bimgDTO",bimgDTO);

        return "material/read";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String modify(Long num,Model model){
        model.addAttribute("materialDTO",materialService.read(num));
        return "material/modify";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String modify(@Valid MaterialDTO materialDTO, BindingResult bindingResult, MultipartFile multipartFile
            ,BimgDTO bimgDTO)
    {

        if (materialDTO.getNum() == null) {
            log.info("왔어? 포스트긴해 근디 ID값없어서 왔다?");
            bindingResult.rejectValue("num", "error.materialDTO", "ID 값이 필요합니다.");
            return "material/modify";
        }

        log.info(materialDTO);
        log.info(bimgDTO);
        if(bindingResult.hasErrors()){
            log.info("에러");
            log.info(bindingResult.getAllErrors());
            return "material/modify";
        }

        materialService.update(materialDTO,  multipartFile , bimgDTO);


        return "redirect:/material/list";
    }
    @PreAuthorize("isAuthenticated()")
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