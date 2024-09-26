package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@EntityListeners(value = {AuditingEntityListener.class})
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MaterialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matNum;

    @Column(length = 10,nullable = false)
    private String matName;//자재명
    @Column(nullable = false,unique = true)
    private String matCode;//자재코드
    @Column(nullable = false)
    private int matAmount;//수량
    @Column(nullable = false)
    private int matPrice;//가격
    @Column(nullable = false)
    private String matBuyPlace;//매입처
    @Column(nullable = false)
    private String matBuyNum;//매입처 사업자번호
    @Column(nullable = false)
    private LocalDate matBuyDate;//매입일자
    @Column(length = 50,nullable = false)
    private String matText;//메모
    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne
    @JoinColumn(name = "mno")
    private UsersEntity usersEntity;
    private String writer;






}