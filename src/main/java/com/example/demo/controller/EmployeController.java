package com.example.demo.controller;


import com.example.demo.dto.*;
import com.example.demo.service.EimgService;
import com.example.demo.service.EmployeService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/employe")
public class EmployeController {

    private final UserService userService;
    private final EmployeService employeService;
    private final EimgService eimgService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/main")
    public String employe_main(Model model,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               Principal principal,
                               @ModelAttribute AdminSearchDTO adminSearchDTO, //유저정보
                               @RequestParam(value = "userid", required = false) String userid) {

        ////////////////////////////////////////////////////////////////////////
        ////////////////////////////// 각종 초기화 ///////////////////////////////
        ////////////////////////////////////////////////////////////////////////
        List<UsersDTO> list = new ArrayList<>(); //list 초기화
        model.addAttribute("usDTO", adminSearchDTO); //검색폼 바인딩

        //log.info(adminSearchDTO);

        ////////////////////////////////////////////////////////////////////////
        ////////////////////////////// 검색 구간 /////////////////////////////////
        ////////////////////////////////////////////////////////////////////////

        if(adminSearchDTO.getKeyword() != null) {
            try {
                if (adminSearchDTO.getType().equals("id_se")) {
                    list = new ArrayList<>(userService.getIdUser("%" + adminSearchDTO.getKeyword() + "%"));
                } else if (adminSearchDTO.getType().equals("ea_se")) {
                    list = new ArrayList<>(userService.getEaUser("%" + adminSearchDTO.getKeyword() + "%"));
                } else if (adminSearchDTO.getType().equals("bnu_se")) {
                    list = new ArrayList<>(userService.getB2User("%" + adminSearchDTO.getKeyword() + "%"));
                } else {
                    list = new ArrayList<>(userService.getAllUser());
                }
            } catch (Exception e) {
                model.addAttribute("err", "유저 정보를 찾을 수 없습니다.");
            }
        }
        else {

            /////////////////////////////////////////////////////////////////////////
            /////////////////////// 유저 정보 및 페이징 처리구간 /////////////////////////
            /////////////////////////////////////////////////////////////////////////
            String username = principal.getName(); //세션 사용자의 이름을 가져옴
            UsersDTO userDTO = userService.getUser(username); //세션 사용자의 이름으로 사용자 정보를 담아냄
            String CompanyNumber = userDTO.getB2bnumber(); //사업자번호

            //리스트에 세션사용자의 사업자번호로 되어있는 모든 사람의 정보를 리스트에 담아냄
            list = new ArrayList<>(userService.getB2User(CompanyNumber));
        }


        try{
            //List<UsersDTO> usersDTOList = userService.getAllUser(); //모든 정보 가져옴
            //컨틀로러에 메서드를 직접 적어서 호출함, 유저 정보, 페이지를 10개씩 잘라서 배열에 저장
            List<UsersDTO[]> paginatedUserList = getPaginatedUserList(list, 10);

            model.addAttribute("", new AdminSearchDTO()); //검색폼 바인딩

            // 현재 페이지에 해당하는 데이터를 모델로 바인딩
            if (page <= paginatedUserList.size()) {
                model.addAttribute("userDTOList", paginatedUserList.get(page - 1));
            } else {
                model.addAttribute("err", "ERROR: 유효하지 않은 페이지 번호입니다.");
            }

            // 총 페이지 수와 현재 페이지 정보를 모델에 추가
            model.addAttribute("totalPages", paginatedUserList.size());
            model.addAttribute("currentPage", page);


            //클릭한 유저 데이터 DTO에 저장해서 바인딩
            try {
                //첫번째 사람을 userid에 미리 저장해서 값을 가져옴
                if(userid == null || "null".equals(userid) || userid.isEmpty()) {

                    model.addAttribute("bindingDTO", new EmployeMergeDTO());
                }
                else {
                    //UserEntity정보 담아오기
                    UsersDTO usersDTO = userService.getUser(userid);

                    //EmployeEntity정보 담아오기
                    EmployeDTO empDTO = employeService.getemp(usersDTO.getMno());

                    //데이터 가공
                    EmployeMergeDTO employeMergeDTO = new EmployeMergeDTO();

                    //유저DTO로 값을 분리
                    employeMergeDTO.setUserid(usersDTO.getUserid());
                    employeMergeDTO.setName(usersDTO.getName());
                    employeMergeDTO.setAge(usersDTO.getAge());
                    employeMergeDTO.setGender(usersDTO.getGender());
                    employeMergeDTO.setEmail(usersDTO.getEmail());
                    employeMergeDTO.setPhone(usersDTO.getPhone());
                    employeMergeDTO.setB2bname(usersDTO.getB2bname());
                    employeMergeDTO.setB2baddr(usersDTO.getB2baddr());
                    employeMergeDTO.setB2bexpont(usersDTO.getB2bexpont());
                    employeMergeDTO.setB2bemail(usersDTO.getB2bemail());
                    employeMergeDTO.setB2bcontact(usersDTO.getB2bcontact());
                    employeMergeDTO.setB2bnumber(usersDTO.getB2bnumber());

                    //사원DTO로 값을분리
                    employeMergeDTO.setEno(empDTO.getEno());
                    employeMergeDTO.setJob(empDTO.getJob());
                    employeMergeDTO.setRank(empDTO.getRank());
                    employeMergeDTO.setJoin_date(empDTO.getJoin_date());
                    employeMergeDTO.setSal(empDTO.getSal());


                    model.addAttribute("bindingDTO", employeMergeDTO);
                }
            }
            catch (Exception e) {
                model.addAttribute("bindingDTO", new EmployeMergeDTO());
                model.addAttribute("err", e.toString() );
            }

            return "employe/main";
        }
        catch (Exception e) {
            model.addAttribute("bindingDTO", new EmployeMergeDTO());
            model.addAttribute("err" , "ERROR : 현재 유저정보가 없습니다.");
            return "employe/main";
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


    //DTO합체

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/employe_pro")
    public String employe_pro(Model model,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              Principal principal,
                              @RequestParam(value = "userid", required = false) String userid,
                              @ModelAttribute EmployeMergeDTO bindingDTO,
                              @ModelAttribute AdminSearchDTO usDTO, //유저정보,
                              BindingResult bindingResult, EimgDTO eimgDTO
    ) {

        model.addAttribute("usDTO", usDTO);//검색폼 바인딩

        ////////////////////////////////////////////////////////////////////////
        ////////////////////////////// 각종 초기화 ///////////////////////////////
        ////////////////////////////////////////////////////////////////////////

        String username = principal.getName(); //세션 사용자의 이름을 가져옴
        UsersDTO userDTO = userService.getUser(username); //세션 사용자의 이름으로 사용자 정보를 담아냄
        String CompanyNumber = userDTO.getB2bnumber(); //사업자번호

        List<UsersDTO> list = new ArrayList<>(); //list 초기화

        //리스트에 세션사용자의 사업자번호로 되어있는 모든 사람의 정보를 리스트에 담아냄
        list = new ArrayList<>(userService.getB2User(CompanyNumber));


        try{
            ////////////////////////////////////////////////////////////////////////
            /////////////////////////// 정보 수정 구간 ///////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            String getuserid = bindingDTO.getUserid();

            UsersDTO editDTO = userService.getUser(getuserid); //바인딩된 userid 로 우선 정보를 가져옴

            //null 이 있으면 오류를 보냄
            if(bindingDTO.getUserid()== null ||
                    bindingDTO.getName() == null ||
                    bindingDTO.getAge() == null ||
                    bindingDTO.getGender() == null ||
                    bindingDTO.getEmail() == null ||
                    bindingDTO.getPhone() == null ||
                    bindingDTO.getB2bname() == null ||
                    bindingDTO.getB2baddr() == null ||
                    bindingDTO.getB2bexpont() == null ||
                    bindingDTO.getB2bemail() == null ||
                    bindingDTO.getB2bcontact() == null ||
                    bindingDTO.getB2bnumber() == null

            ) {

                model.addAttribute("err" , "ERROR : 수정하지 못한 값이 있습니다.");
            }
            else {
                editDTO.setName(bindingDTO.getName());
                editDTO.setAge(bindingDTO.getAge());
                editDTO.setGender(bindingDTO.getGender());
                editDTO.setEmail(bindingDTO.getEmail());
                editDTO.setPhone(bindingDTO.getPhone());
                editDTO.setB2bname(bindingDTO.getB2bname());
                editDTO.setB2baddr(bindingDTO.getB2baddr());
                editDTO.setB2bexpont(bindingDTO.getB2bexpont());
                editDTO.setB2bemail(bindingDTO.getB2bemail());
                editDTO.setB2bcontact(bindingDTO.getB2bcontact());
                editDTO.setB2bnumber(bindingDTO.getB2bnumber());

                //log.info("LOG!!! : " + bindingDTO.toString());
                //유저 프로필 저장
                log.info("여기는employe 컨트롤러 eimg DTO 확인"+eimgDTO);

                userService.updateUser2(getuserid, editDTO);

                //현재 수정할 것의 Mno값
                Long templong = userService.getUser(getuserid).getMno();


                EmployeDTO employeDTO = new EmployeDTO();
                employeDTO.setJob(bindingDTO.getJob());
                employeDTO.setJoin_date(bindingDTO.getJoin_date());
                employeDTO.setRank(bindingDTO.getRank());
                employeDTO.setSal(bindingDTO.getSal());
                //emp테이블에 값 저장

                log.info("LOG!!! : " + bindingDTO);

                employeService.setemp(templong, employeDTO);

                model.addAttribute("scc" , "정보가 수정 되었습니다.");

            }



            /////////////////////////////////////////////////////////////////////////
            /////////////////////// 유저 정보 및 페이징 처리구간 /////////////////////////
            /////////////////////////////////////////////////////////////////////////

            //List<UsersDTO> usersDTOList = userService.getAllUser(); //모든 정보 가져옴
            //컨틀로러에 메서드를 직접 적어서 호출함, 유저 정보, 페이지를 10개씩 잘라서 배열에 저장
            List<UsersDTO[]> paginatedUserList = getPaginatedUserList(list, 10);

            model.addAttribute("", new AdminSearchDTO()); //검색폼 바인딩

            // 현재 페이지에 해당하는 데이터를 모델로 바인딩
            if (page <= paginatedUserList.size()) {
                model.addAttribute("userDTOList", paginatedUserList.get(page - 1));
            } else {
                model.addAttribute("err", "ERROR: 유저 정보를 찾을 수 없습니다.");
            }

            // 총 페이지 수와 현재 페이지 정보를 모델에 추가
            model.addAttribute("totalPages", paginatedUserList.size());
            model.addAttribute("currentPage", page);


            //클릭한 유저 데이터 DTO에 저장해서 바인딩
            try {
                //첫번째 사람을 userid에 미리 저장해서 값을 가져옴
                if(userid == null || "null".equals(userid) || userid.isEmpty()) {
                    model.addAttribute("getDTO", new EmployeMergeDTO());
                }
                else {
                    //UserEntity정보 담아오기
                    UsersDTO usersDTO = userService.getUser(userid);
                    bindingDTO.setMno(usersDTO.getMno());

                    model.addAttribute("bindingDTO", bindingDTO);

                }
            }
            catch (Exception e) {
                model.addAttribute("bindingDTO", bindingDTO);
                model.addAttribute("err", e.toString());
            }

            return "employe/main";
        }
        catch (Exception e) {
            model.addAttribute("bindingDTO", bindingDTO);
            model.addAttribute("err" , e.toString());
            //model.addAttribute("err" , "ERROR : 현재 유저정보가 없습니다.");
            return "employe/main";
        }
    }
}
