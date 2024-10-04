package com.example.demo.controller;

import com.example.demo.dto.AdminSearchDTO;
import com.example.demo.dto.EimgDTO;
import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.UsersEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EimgService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

    private final UserService userService;

    private final EimgService eimgService;
    private final UserRepository userRepository;


    //메인페이지
    @GetMapping("/")
    public String home(Principal principal, Model model) {
        try {
            String name = principal.getName();
            model.addAttribute("name", name);
        }
        catch (Exception e) {

        }
        return "main";
    }

    @GetMapping("/main")
    public String home2(Principal principal, Model model) {
        try {
            String name = principal.getName();
            model.addAttribute("name", name);
        }
        catch (Exception e) {

        }
        return "main";
    }

    //회사 소개
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    //로그인 페이지 전환
    //@GetMapping("/login")
    //public String login() {
    //    return "login";
    //}
    @GetMapping("/map")//오시는길
    public  String map() {return "map";}

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //현재 인증된 사용자의 정보를 가져옴

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            // 이미 로그인된 상태이면 메인 페이지로 리다이렉트
            response.sendRedirect("/");
            return null;
        }
        return "login";
    }

    //권한 필요 페이지
    @GetMapping("/acc-denied")
    public String acc() {return "acc-denied"; }


    //관리자 페이지
    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/adminPage")
    public String adminPage (Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        try{
            List<UsersDTO> usersDTOList = userService.getAllUser(); //모든 정보 가져옴
            //컨틀로러에 메서드를 직접 적어서 호출함, 유저 정보, 페이지를 10개씩 잘라서 배열에 저장
            List<UsersDTO[]> paginatedUserList = getPaginatedUserList(usersDTOList, 10);

            model.addAttribute("seDTO", new AdminSearchDTO()); //검색폼 바인딩

            // 현재 페이지에 해당하는 데이터를 모델로 바인딩
            if (page <= paginatedUserList.size()) {
                model.addAttribute("userDTOList", paginatedUserList.get(page - 1));
            } else {
                model.addAttribute("err", "ERROR: 유효하지 않은 페이지 번호입니다.");
            }

            // 총 페이지 수와 현재 페이지 정보를 모델에 추가
            model.addAttribute("totalPages", paginatedUserList.size());
            model.addAttribute("currentPage", page);

            return "adminPage";
        }
        catch (Exception e) {
            model.addAttribute("err" , "ERROR : 현재 유저정보가 없습니다.");
            return "adminPage";
        }

    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/admin_se")
    public String admin_idse (Model model, @RequestParam(value = "page", defaultValue = "1") int page, @ModelAttribute AdminSearchDTO adminSearchDTO) {

        model.addAttribute("seDTO", adminSearchDTO); //검색폼 바인딩

        List<UsersDTO> list = new ArrayList<>(); //list 초기화

        try {
            //아이디로 검색
            if (adminSearchDTO.getType().equals("id")) {
                if(adminSearchDTO.getKeyword().isEmpty()) {
                    list = new ArrayList<>(userService.getAllUser());
                }
                else {
                    list = new ArrayList<>(userService.getIdUser("%" + adminSearchDTO.getKeyword() + "%"));
                }
            }

            //이메일로 검색
            else if (adminSearchDTO.getType().equals("ea")) {
                if(adminSearchDTO.getKeyword().isEmpty()) {
                    list = new ArrayList<>(userService.getAllUser());
                }
                else {
                    list = new ArrayList<>(userService.getEaUser( "%" + adminSearchDTO.getKeyword() + "%"));
                }
            }

            //사업자등록번호로 검색
            else if(adminSearchDTO.getType().equals("b2")) {
                if(adminSearchDTO.getKeyword().isEmpty()) {
                    list = new ArrayList<>(userService.getAllUser());
                }
                else {
                    list = new ArrayList<>(userService.getB2User("%" + adminSearchDTO.getKeyword() + "%"));
                }

            }
            else {
                list = new ArrayList<>(userService.getAllUser());
            }
        }
        catch (Exception e) {
            model.addAttribute("err", "ERROR : 유저를 찾을 수 없습니다.");
            return "adminPage";
        }


        //페이징처리구간
        List<UsersDTO[]> paginatedUserList = getPaginatedUserList(list, 10);
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

            return "adminPage";
        }
        catch (Exception e) {
            model.addAttribute("err", "ERROR : 유저를 찾을 수 없습니다.");
            return "adminPage";
        }
    }

    //페이징처리를 컨트롤러에서 직접 할 것임
    public List<UsersDTO[]> getPaginatedUserList(List<UsersDTO> usersDTOList, int itemsPerPage) {
        int totalRecords = usersDTOList.size();         //UserEntity 가 가진 유저의 총 레코드 수
        int totalPages = totalRecords / itemsPerPage;   //10으로 나눈 몫 || 한 화면에 10개씩 페이지 이동버튼을 보이기위함
        int remainder = totalRecords % itemsPerPage;    //10으로 나눈 나머지

        if (remainder != 0) { //나머지가 0이 아닐 경우 작동
            totalPages += 1; // 총 페이지 수 +1 은 2차원 배열의 크기를 지정하기 위함임 0부터 시작하기 때문
        }

        UsersDTO[][] paginatedArray = new UsersDTO[totalPages][]; //UsesDTO 타입인 2차원 배열 생성
        for (int i = 0; i < totalPages; i++) {
            int start = i * itemsPerPage; //배열 시작점
            int end = Math.min(start + itemsPerPage, totalRecords); //배열 끝점
            paginatedArray[i] = usersDTOList.subList(start, end).toArray(new UsersDTO[0]); //2차원 배열 최대크기 지정 및 값저장
        }

        return Arrays.asList(paginatedArray); //가공된 2차원 배열 정보 리턴
    }


    //유저정보 불러온 페이지
    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/adminpage_chg")//정보 가져오기
    public String adminpage_chg(@RequestParam String userid, Model model
            , MultipartFile multipartFile) {
       log.info(userid);
       log.info(userid);
       log.info(userid);
       log.info(userid);
        Long u_mno = userRepository.findByUserid(userid).get().getMno();
       EimgDTO eimgDTO = eimgService.read(u_mno);
        if(multipartFile != null){
            log.info(multipartFile.getOriginalFilename());
        }else {
            log.info("암것두 없다,,,,",eimgDTO);
        }

        try {
            UsersDTO usersDTO = userService.getUser(userid);


            model.addAttribute("eimgDTO",eimgDTO);
            model.addAttribute("userDTO", usersDTO);
            return "adminpage_pro";
        }
        catch (Exception e) {

            model.addAttribute("userDTO", new UsersDTO());
            model.addAttribute("eimgDTO", new EimgDTO());
            model.addAttribute("err", "유저 정보를 찾을 수 없습니다.");
            return "adminpage_pro";
        }
    }
}
