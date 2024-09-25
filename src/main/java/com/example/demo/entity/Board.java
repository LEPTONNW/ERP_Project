package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity //엔티티임을 명시
@Getter
@Setter
@ToString
@NoArgsConstructor //기본생성자
@AllArgsConstructor //모든필드값을 가지고 있는 생성자


public class Board extends BaseEntity {
    //pk
    //제목 내용 작성자 작성날짜
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno; //글번호 pk

    @Column(length = 50, nullable = false)
    private String title; //제목

    @Column (columnDefinition = "text")
    private String content;

    @ManyToOne
    @JoinColumn(name = "mno")
    private UsersEntity usersEntity;


    private String writer;

    //등록일자 혹은 만든이 기타등등이 들어감
}
