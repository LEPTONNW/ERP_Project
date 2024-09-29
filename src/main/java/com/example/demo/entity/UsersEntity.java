package com.example.demo.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
    @Size (min = 2, max =50, message = "사용자 아이디는 2~50글자 사이로만 가능합니다.")
    @NotEmpty(message = "사용자 아이디는 필수 항목입니다.")
    private String userid;

    @Column(length = 255, nullable = false)
    @NotEmpty(message = "사용자 비밀번호는 필수 항목 입니다.")
    private String pass; // 비밀번호
    @Size (min = 2, max =50, message = "이름은 2~50글자 사이로만 가능합니다.")
    @NotEmpty(message = "사용자 이름은 필수 항목 입니다.")
    @Column(nullable = false)
    private String name; // 이름

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate age; //생년월일
    //@NotEmpty(message = "게이임?? 씹련")
    @Column(nullable = false)
    private String gender; //성별

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "사용자 이메일은 필수 항목 입니다.")
    private String email; //이메일

    @Column(nullable = false)
    private String phone; //연락처

    @Column(nullable = false)
    private String b2bname; //상호명

    @Column(nullable = false)
    private String b2baddr; //회사주소

    @Column(  nullable = false)
    private String b2bexpont; //대표자 성함

    @Column(nullable = false)
    private String b2bemail; //대표 이메일

    @Column(nullable = false)
    private String b2bcontact; //회사 연락처

    @Column(nullable = false)
    @NotEmpty(message = "사업자등록번호는 필수 항목 입니다.")
    private String b2bnumber; //사업자등록번호

    @Column
    private String permission;  //SuperAdmin, Admin, User 권한 여부
}
