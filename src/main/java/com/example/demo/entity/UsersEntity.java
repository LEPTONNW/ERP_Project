package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity //엔티티임을 명시
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든필드값을 가지고 있는 생성자
public class UsersEntity extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;   //글번호 pk

    @Column(length = 15, nullable = false, unique = true)
    private String pass; // 비밀번호

    @Column(length = 10, nullable = false)
    private String name; // 이름

    @Column(nullable = false)
    private int age; //나이

    @Column(nullable = false)
    private String gender; //성별

    @Column(length = 20, nullable = false)
    private String email; //이메일

    @Column(length = 10, nullable = false)
    private String phone; //연락처

    @Column(length = 20, nullable = false,unique = true)
    private String B2BName; //상호명

    @Column(length = 50, nullable = false,unique = true)
    private String B2BAddr; //회사주소

    @Column(length = 10, nullable = false)
    private String B2BExpont; //대표자 성함

    @Column(length = 20, nullable = false)
    private String B2BEmail; //대표 이메일

    @Column(length = 10, nullable = false,unique = true)
    private String B2BContact; //회사 연락처

    @Column(length = 20, nullable = false, unique = true)
    private String B2BNumber; //사업자등록번호


}
