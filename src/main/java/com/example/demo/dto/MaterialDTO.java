package com.example.demo.dto;


import com.example.demo.entity.MaterialEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Data
public class MaterialDTO {
    private Long num;
    private String matName;     //자재명
    private String matCode;     //자재코드
    private int matAmount;      //수량
    private int matPrice;       //가격
    private String matBuyPlace; //매입처
    private String matBuyNum;   //매입처 사업자 번호
    private LocalDate matBuyDate;  //매입일자
    private String matText;     //메모

    private MultipartFile imgFile;
    private String imgUrl;







    private String writer;
    private LocalDate regidate;
    private LocalDate modDate;
}