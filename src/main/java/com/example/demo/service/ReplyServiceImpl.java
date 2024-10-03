package com.example.demo.service;

import com.example.demo.dto.ReplyDTO;
import com.example.demo.entity.Reply;
import com.example.demo.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ModelMapper mapper = new ModelMapper();  // final로 변경
    private final ReplyRepository replyRepository; // final로 변경


    @Override
    public ReplyDTO replyCreate(ReplyDTO replyDTO) {

        // ReplyDTO를 Reply 엔티티로 변환
        Reply reply = mapper.map(replyDTO, Reply.class);

        // 현재 날짜 및 시간 설정
        LocalDate now = LocalDate.now();
        reply.setModDate(now);
        reply.setRegidate(now);
        //reply.setRwriter("작성자"); // 작성자 정보 설정 (현재 사용자 정보로 수정 필요)


        // Reply를 데이터베이스에 저장
        Reply savedReply = replyRepository.save(reply);

        return replyDTO;
    }

}
