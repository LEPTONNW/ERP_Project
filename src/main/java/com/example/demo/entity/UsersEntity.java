package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity //엔티티임을 명시
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든필드값을 가지고 있는 생성자
public class UsersEntity extends BaseEntity {


    //현재 유니크 유저ID, 이메일
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;   //글번호 pk

    @Column (length = 30, nullable = false, unique = true)
    private String userid;

    @Column(length = 255, nullable = false)
    private String pass; // 비밀번호

    @Column(length = 30, nullable = false)
    private String name; // 이름

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate age; //생년월일

    @Column(nullable = false)
    private String gender; //성별

    @Column(length = 20, nullable = false, unique = true)
    private String email; //이메일

    @Column(length = 30, nullable = false)
    private String phone; //연락처

    @Column(length = 20, nullable = false)
    private String b2bname; //상호명

    @Column(length = 100, nullable = false)
    private String b2baddr; //회사주소

    @Column(length = 10, nullable = false)
    private String b2bexpont; //대표자 성함

    @Column(length = 20, nullable = false)
    private String b2bemail; //대표 이메일

    @Column(length = 30, nullable = false)
    private String b2bcontact; //회사 연락처

    @Column(length = 20, nullable = false)
    private String b2bnumber; //사업자등록번호

    @Column
    private String permission;  //SuperAdmin, Admin, User 권한 여부
}
