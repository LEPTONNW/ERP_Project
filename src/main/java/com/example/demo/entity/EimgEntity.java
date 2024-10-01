package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EimgEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eino;//Pk
    private String picname;//사진이름
    private  String oripicname;//사진의 오리지널 이름
    private  String pic_url;// 사진의 저장된 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mno")// mno유저 꺼 받아올꺼임
    private UsersEntity usersEntity;//누구의 사진?



}
