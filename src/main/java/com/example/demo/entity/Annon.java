package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Annon extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno; //글번호 pk

    @Column(length = 50, nullable = false)
    private String title; //제목

    @Column (columnDefinition = "text")
    private String content;

    private String writer;

    @ManyToOne
    @JoinColumn(name = "mno")
    private UsersEntity usersEntity;


}
