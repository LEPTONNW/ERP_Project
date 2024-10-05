package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
//근데 이걸 다른 게시판 검색에도 쓸 것임 (제목, 내용, 작성자) 로
public class AdminSearchDTO {
    private String type;    //검색 타입 (아이디, 이메일, 사업자 등록번호)

    private String keyword; //검색어
}
