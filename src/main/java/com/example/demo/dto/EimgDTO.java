package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EimgDTO {
    private Long eino;
    private String picname;
    private  String oripicname;
    private  String pic_url;
    private UsersDTO usersDTO;

    public EimgDTO setUserSDTO(UsersDTO usersDTO){
        this.usersDTO = usersDTO;
        return this;
    }
}
