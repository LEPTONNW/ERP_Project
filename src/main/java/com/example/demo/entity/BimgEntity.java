package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity //엔티티임을 명시
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든필드값을 가지고 있는 생성자
public class BimgEntity extends BaseEntity {
    //pk
    //제목 내용 작성자 작성날짜

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bino;   //글번호 pk

    private String imgname;//사진이름
    private String oriimgname;//오리지널 name
    private String img_url;//사진 경로


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "num") // num자재 글이요
    private MaterialEntity materialEntity;//어떤 놈의 사진


    //등록일자 혹은 만든이 기타등등이 들어감


}
