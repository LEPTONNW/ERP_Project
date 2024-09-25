package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UsersDTO {
    //회원정보
    private String id;          //아이디
    private String pass;        //비밀번호
    private String name;        //이름
    private int age;            //나이
    private String gender;      //성별
    private String email;       //이메일
    private String phone;       //연락처


    //회사정보
    private String B2BName;     //상호명
    private String B2BAddr;     //회사주소
    private String B2BExpont;   //대표자 성함
    private String B2BEmail;    //대표이메일
    private String B2BContact;  //회사연락처
    private String B2BNumber;   //사업자등록번호
}
