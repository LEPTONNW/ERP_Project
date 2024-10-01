package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class AdminSearchDTO {
    private String type;    //검색 타입 (아이디, 이메일, 사업자 등록번호)

    private String keyword; //검색어
}
