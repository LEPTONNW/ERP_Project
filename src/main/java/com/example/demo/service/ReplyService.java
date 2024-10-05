package com.example.demo.service;

import com.example.demo.dto.ReplyDTO;
import com.example.demo.dto.UsersDTO;

import java.util.List;

public interface ReplyService {

    public ReplyDTO replyCreate(ReplyDTO replyDTO, String userid);

    public List<ReplyDTO> replyRead(Long bno);

    public ReplyDTO replyOne(Long rno);

    public Long readModify(ReplyDTO replyDTO);

    public void replyDelete(ReplyDTO replyDTO);


    //답변 수정기능
    //답변 번호 replyDTO에 이미 있음

}
