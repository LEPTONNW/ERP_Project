package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BimgDTO {
    private Long bino;
    private String imgname;//사진이름
    private String oriimgname;//오리지널 name
    private String img_url;//사진 경로
    private MaterialDTO materialDTO;//엔티티로 변환할꺼야


    public BimgDTO setMaterialDTO(MaterialDTO materialDTO) {
        this.materialDTO = materialDTO;
        return this;
    }
}
