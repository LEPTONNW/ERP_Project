package com.example.demo.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MaterialDTO {
    private int matNum;         //글번호
    private String matName;     //자재명
    private String matCode;     //자재코드
    private int matAmount;      //수량
    private int matPrice;       //가격
    private String matBuyPlace; //매입처
    private String matBuyNum;   //매입처 사업자 번호
    private String matBuyDate;  //매입일자
    private String matText;     //메모
}




//일단은 DTO는 이정도? 이걸 표로 작성하기만 하면 될듯요?

