package com.example.demo.service;

import com.example.demo.dto.ReplyDTO;
import com.example.demo.dto.UsersDTO;

public interface ReplyService {

    public ReplyDTO replyCreate(ReplyDTO replyDTO);

    UsersDTO getUser(String userid); //내정보 가져오기용
}
