package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {

    private Long rno;

    private String rtitle;
    private String rcontent;
    private String rwriter;

    private Long mno;
    private Long bno;



}
