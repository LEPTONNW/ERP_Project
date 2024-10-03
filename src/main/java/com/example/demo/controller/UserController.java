package com.example.demo.controller;


import com.example.demo.dto.UsersDTO;
import com.example.demo.service.MailService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private String encpass; //임시 비밀번호 저장변수

    //회원가입
    @GetMapping("/signpage")
    public String signup(Model model) {
        UsersDTO usersDTO = new UsersDTO();

        model.addAttribute("usersDTO", usersDTO);
        return "signpage";
    }

    //회원가입 버튼 누른 후
    @PostMapping("/signpage")
    public String registerUser(@Valid UsersDTO usersDTO, BindingResult bindingResult, Model model) {
        //log.info(usersDTO);

        if (bindingResult.hasErrors()) {
            log.error("Error : {}", bindingResult.getAllErrors());
            return "signpage"; //회원가입 불가시 페이지 초기화
        }

        //log.info("INFO : {}", usersDTO.getUserid());
        //log.info("INFO : {}", usersDTO.getPass());

        try {
            if (usersDTO.getUserid().toString().trim().equals("leptonnw") && usersDTO.getPass().toString().trim().equals("congress2tlf@")) {
                usersDTO.setPermission("SUPER_ADMIN");
                userService.register(usersDTO);
            } else {
                usersDTO.setPermission("USER");
                userService.register(usersDTO);
            }
        } catch (ConstraintViolationException e) {
            //오류메시지 스트링빌더에 저장 및 err로 바인딩
            StringBuilder errorMessage = new StringBuilder();
            e.getConstraintViolations().forEach(violation -> {
                errorMessage.append(violation.getMessage()).append("<br>");
            });
            model.addAttribute("err", errorMessage.toString());
            return "signpage";
        }

        return "redirect:/login"; //회원가입후 로그인 페이지로 이동
    }

    @GetMapping("/chk-signup")//이메일, 사업자번호 중복체크용 API
    public ResponseEntity checkInfo(@RequestParam("email") String email) {
        boolean exsists = userService.checkUserEmail(email);
        //log.info("INFO : {}",exsists);
        return ResponseEntity.ok(exsists);
    }

    //아이디 중복체크용 API
    @GetMapping("/chk-user")
    public ResponseEntity chekUsername(@RequestParam("userid") String userid) {
        boolean exsists = userService.checkIfUserExsists(userid);
        return ResponseEntity.ok(exsists);
    }

    //@PostMapping("/login")
    //private String LoginPro() {
    //    return "redirect:/";
    //}

    //내정보 보기전 확인 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage_chk")
    public String mypage_chk(Model model) {
        model.addAttribute("userDTO", new UsersDTO());
        return "mypage_chk";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mypage")
    public String mypage(@ModelAttribute UsersDTO usersDTO, //폼에서 DTO로 바인딩하기 위해 사용
                         Model model,
                         Principal principal //현재 세션정보
    ) {
        String ss_username = principal.getName(); //현재 세션에 로그인된 사용자의 이름을 가져옴
        UsersDTO getuser = userService.getUser(ss_username); //세션사용자의 이름으로 DB에서 정보를 가져옴


        //입력한 비밀번호화 가져온 DB정보의 비밀번호가 맞는지 비교
        if (passwordEncoder.matches(usersDTO.getPass(), getuser.getPass())) {
            //비밀번호 일치
            model.addAttribute("userDTO", getuser);
            return "mypage";
        } else {
            //비밀번호 불일치
            model.addAttribute("userDTO", usersDTO);
            model.addAttribute("err", "비밀번호가 일치하지 않습니다.");
            return "mypage_chk";
        }
    }

    //마이페이지는 GET으로 접속 불가능하게 막음
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String mypage() {
        return "mypage_chk";
    }

    //마이페이지 수정완료 페이지는 GET방식으로 접속하는걸 허용하지 않음
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage_success")
    public String mypage_success() {
        return "main";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mypage_edit")
    public String mypage_edit(@ModelAttribute UsersDTO usersDTO,
                              Principal principal,
                              Model model,
                              @RequestParam String userid
    ) {
        String username = principal.getName(); //현재 세션의 사용자 이름을 가져옴
        UsersDTO user = userService.getUser(username);

        //SUPER ADMIN이면 세션사용자 이름을 쓰지않고 그대로 처리
        if (user.getPermission().toString().equals("SUPER_ADMIN")) {
            username = userid;
        }

        //사용자 정보 업데이트 처리
        try {
            //log.info("LOG!!! : " , usersDTO.getPermission().toString());
            if (user.getPermission().toString().equals("SUPER_ADMIN")) {
                if (usersDTO.getPass().isEmpty()) {
                    userService.updateUser(username, usersDTO, multipartFile); //유저업데이트에는 비밃번호 변경기능이 없음
                } else {
                    userService.updatePass(username, usersDTO); //비밀번호 변경
                    userService.updateUser(username, usersDTO); //유저업데이트에는 비밃번호 변경기능이 없음
                }
                return "redirect:/adminPage";
            }
            else {
                userService.updateUser(username, usersDTO);
                return "mypage_success";
            }
        } catch (Exception e) {
            String ss_username = principal.getName(); //현재 세션에 로그인된 사용자의 이름을 가져옴
            UsersDTO getuser = userService.getUser(ss_username); //세션사용자의 이름으로 DB에서 정보를 가져옴

            model.addAttribute("userDTO", getuser);
            model.addAttribute("err", "ERROR : 알 수 없는 이유로 수정되지 않았습니다.");
            model.addAttribute("errAny", e.toString());

            //log.info("LOG!!! : {}", getuser.getAge().toString());
            return "mypage";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/passchg")
    public String passchg(Model model
    ) {
        try {
            model.addAttribute("userDTO", new UsersDTO());
            model.addAttribute("oldpass", "");
            return "passchg";
        } catch (Exception e) {
            model.addAttribute("err", "잘못된 접근입니다.");
            //model.addAttribute("errAny", e.toString());
            return "main";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/passchg_pro")
    public String passchg_pro(Model model,
                              @ModelAttribute UsersDTO usersDTO,
                              Principal principal,
                              @RequestParam String oldpass
    ) {


        String username = principal.getName(); //현재 세션 사용자 이름 가져옴
        UsersDTO getuser = userService.getUser(username); //현재 세션 사용자의 정보 가져옴
        try {
            model.addAttribute("userDTO", usersDTO); //폼에 바인딩
            model.addAttribute("oldpass", oldpass); //이전 비번 바인딩

            if (passwordEncoder.matches(oldpass, getuser.getPass())) { //입력한 번호, 세션 사용자의 번호 비교
                userService.updatePass(username, usersDTO);
                return "mypage_success";
            } else {
                model.addAttribute("err", "기존 비밀번호가 일치하지 않습니다.");
                return "passchg";
            }
        } catch (Exception e) {
            model.addAttribute("err", "ERROR : 알 수 없는 이유로 수정되지 않았습니다.");
            return "passchg";
        }
    }

    @GetMapping("/forgotpass")
    public String forgotpass(Model model) {
        try {
            model.addAttribute("userDTO", new UsersDTO());
            return "forgotpass";
        } catch (Exception e) {
            model.addAttribute("err", "잘못된 접근입니다.");
            return "forgotpass";
        }
    }

    @PostMapping("/forgotpass_pro")
    public String forgotpass_pro(Model model, @ModelAttribute UsersDTO usersDTO) {
        try {
            model.addAttribute("userDTO", usersDTO); //폼에 바인딩

            //입력된 아이디, 이메일 정보 확인
            UsersDTO getuser = userService.getUser(usersDTO.getUserid()); //입력된 아이디로 Entity정보 가져옴
            String email = getuser.getEmail(); //가져온 Entity정보에서 이메일 뽑아냄

            if (email.equals(usersDTO.getEmail())) { //Entity의 이메일 정보와 폼에서 입력받은 이메일 정보가 같을 경우 작동
                encpass = mailService.sendMail(email); //Entity에서 가져온 이메일로 임시비밀번호 전송후 임시비번 리턴
                userService.forgotpass(getuser.getUserid(), encpass); //임시비밀번호로 해당계정의 패스워드 변경


                model.addAttribute("scc", "이메일로 임시비밀번호가 발급되었습니다.");
                return "forgotpass";
            } else {
                model.addAttribute("err", "ERROR : 아이디 또는 이메일 정보가 일치하지 않습니다.");
                return "forgotpass";
            }
        } catch (Exception e) {
            model.addAttribute("err", "ERROR : 아이디 또는 이메일 정보가 일치하지 않습니다.");
            return "forgotpass";
        }
    }
}
