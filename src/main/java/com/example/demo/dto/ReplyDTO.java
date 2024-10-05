package com.example.demo.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {

    private Long rno;


    private String rcontent;

    private String rwriter;

    private Long mno;

    private Long bno;

    private BoardDTO boardDTO;

    private LocalDate regidate;

    private LocalDate modDate;

    public ReplyDTO setBoardDTO(BoardDTO boardDTO) {
        this.boardDTO = boardDTO;
        return  this;
    }
}
