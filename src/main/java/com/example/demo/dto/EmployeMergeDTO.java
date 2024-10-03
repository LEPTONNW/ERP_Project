package com.example.demo.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class EmployeMergeDTO {
    //회원정보
    private String userid;      //아이디
    private String name;        //이름

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate age;      //생년월일
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

    //사원정보
    private Long eno;               //번호(PK)
    private String job;             //직무
    private String rank;            //직급
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate join_date;    //입사일자
    private int sal;                //연봉

    private Long mno;               //유저테이블 번호 (FK)


}
