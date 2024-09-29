package com.example.demo.service;

import com.example.demo.dto.UsersDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    Long register(UsersDTO userDTO);

    boolean checkIfUserExsists(String userid); //아이디 중복확인용

    boolean checkUserEmail(String email); //회원가입시 이메일 중복확인

    UsersDTO getUser(String userid); //내정보 가져오기용
}
