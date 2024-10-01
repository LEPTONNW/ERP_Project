package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;   // 답변글 번호 pk

    @Column(length = 255, nullable = false)
    private String rtitle;   // 답변 제목

    @Column(columnDefinition = "text")
    private String rcontent;   // 답변 내용

    private String rwriter; // 답변자

    @ManyToOne
    @JoinColumn(name = "mno")
    private UsersEntity mno; // 유저 이름

    @ManyToOne
    @JoinColumn(name = "bno")
    private Board board; // 질문



}
