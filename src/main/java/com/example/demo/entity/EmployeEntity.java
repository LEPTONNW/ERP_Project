package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eno;                   //번호
    @Column
    private String name;                //이름
    @Column
    private int age;                    //나이
    @Column
    private String gender;              //성별
    @Column
    private String email;               //이메일
    @Column
    private String job;                 //직무
    @Column
    private String rank;                //직급
    @Column
    private LocalDate join_date;        //입사일자
    @Column
    private String sal;                 //연봉

    @ManyToOne
    @JoinColumn(name = "mno")
    private UsersEntity usersEntity;
}
