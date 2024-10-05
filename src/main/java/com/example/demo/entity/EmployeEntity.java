package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeEntity {

    @Id //PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eno;                   //번호

    @Column
    private String job;                 //직무

    @Column
    private String rank;                //직급

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate join_date;        //입사일자

    @Column
    private int sal;                 //연봉

    @ManyToOne //FK
    @JoinColumn(name = "mno")
    private UsersEntity mno;
}
