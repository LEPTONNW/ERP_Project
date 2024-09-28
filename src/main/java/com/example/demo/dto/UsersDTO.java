package com.example.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Data
public class UsersDTO {
    //회원정보
    private String userid;      //아이디
    private String pass;        //비밀번호
    private String name;        //이름
    private LocalDate age;            //생년월일
    private String gender;      //성별
    private String email;       //이메일
    private String phone;       //연락처


    //회사정보
    private String b2bname;     //상호명
    private String b2baddr;     //회사주소
    private String b2bexpont;   //대표자 성함
    private String b2bemail;    //대표이메일
    private String b2bcontact;  //회사연락처
    private String b2bnumber;   //사업자등록번호

    //SuperAdmin, Admin, User 권한여부
    private String permission;
}
