package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class EmployeDTO {
    private Long eno;               //번호(PK)

    private String job;             //직무
    private String rank;            //직급
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate join_date;    //입사일자
    private int sal;                //연봉

    private Long mno;               //유저테이블 번호 (FK)
}
