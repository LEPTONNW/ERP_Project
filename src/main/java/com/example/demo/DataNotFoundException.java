package com.example.demo;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


//오류처리
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")

public class DataNotFoundException  extends RuntimeException{
    private static final long UID = 1L;

    public DataNotFoundException(String msg) {
        super(msg);
    }
}
