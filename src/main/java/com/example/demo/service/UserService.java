package com.example.demo.service;

import com.example.demo.dto.UsersDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    Long register(UsersDTO userDTO);

    boolean checkIfUserExsists(String userid); //아이디 중복확인용

    boolean checkUserEmail(String email); //회원가입시 이메일 중복확인

    UsersDTO getUser(String userid); //내정보 가져오기용

    UsersDTO updateUser(String userid, UsersDTO usersDTO);//마이프로필 변경

    UsersDTO updatePass(String userid, UsersDTO usersDTO); //비밀번호 변경

    String forgotpass(String userid, String pass); //임시비밀번호 발급

    List<UsersDTO> getAllUser(); //모든 유저 정보 담아오기
}
