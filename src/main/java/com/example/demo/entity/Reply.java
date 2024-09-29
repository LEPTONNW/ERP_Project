package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity //엔티티임을 명시
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든필드값을 가지고 있는 생성자
public class Reply extends BaseEntity {
    //pk
    //제목 내용 작성자 작성날짜

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nno;   //글번호 pk

    @Column(length = 255, nullable = false)
    private String textp;   //제목


    @ManyToOne
    @JoinColumn(name = "mno")
    private UsersEntity usersEntity;

    @ManyToOne
    @JoinColumn(name = "bno")
    private Board board;

}
