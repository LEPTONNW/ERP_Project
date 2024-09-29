package com.example.demo.dto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class AnnonDTO {

    private Long bno;   //글번호 pk


    @NotNull
    @Size(min = 2, max = 50)
    private String title;   //제목


    @NotEmpty
    @Size(min = 2, max = 500)
    private String content;


    private String writer;

    private LocalDate regidate;

    private LocalDate modDate;
}
