package com.example.demo.service;

import com.example.demo.dto.EimgDTO;
import com.example.demo.dto.UsersDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {
    Long register(UsersDTO userDTO);

    boolean checkIfUserExsists(String userid); //아이디 중복확인용

    boolean checkUserEmail(String email); //회원가입시 이메일 중복확인

    UsersDTO getUser(String userid); //내정보 가져오기용

    UsersDTO updateUser(String userid, UsersDTO usersDTO, MultipartFile multipartFile);//마이프로필 변경

    UsersDTO updatePass(String userid, UsersDTO usersDTO); //비밀번호 변경

    String forgotpass(String userid, String pass); //임시비밀번호 발급

    List<UsersDTO> getAllUser(); //모든 유저 정보 담아오기

    List<UsersDTO> getIdUser(String userid); //관리자페이지 이메일로 검색
    List<UsersDTO> getEaUser(String b2bnumber); //관리자페이지 이메일로 검색
    List<UsersDTO> getB2User(String email); //관리자페이지 사업자번호로 검색

    public boolean register2(UsersDTO usersDTO, MultipartFile multipartFile);

    String delete_user(String userid);

}
