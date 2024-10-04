package com.example.demo.service;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.ReplyDTO;
import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.Board;
import com.example.demo.entity.Reply;
import com.example.demo.entity.UsersEntity;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.ReplyRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    private final UserRepository userRepository;

    private final BoardRepository boardRepository;
    private final ModelMapper mapper = new ModelMapper();


    public ReplyDTO replyCreate(ReplyDTO replyDTO, String userid) {

        Optional<UsersEntity> usersOption = userRepository.findByUserid(userid);
        UsersEntity usersEntity = usersOption.get();

        Optional<Board> optionalBoard = boardRepository.findById(replyDTO.getBno());
        Board board = optionalBoard.get();

        // ReplyDTO를 Reply 엔티티로 변환
        Reply reply = mapper.map(replyDTO, Reply.class);

        reply.setMno(usersEntity);
        reply.setBoard(board);


        //reply.setRwriter("작성자"); // 작성자 정보 설정 (현재 사용자 정보로 수정 필요)


        // Reply를 데이터베이스에 저장
        Reply savedReply = replyRepository.save(reply);
        replyDTO.setRno(reply.getRno());

        return replyDTO;
    }

    public List<ReplyDTO> replyRead(Long bno) {

        List<Reply> replyList = replyRepository.findByBoardBno(bno);

        replyList.forEach(reply -> log.info("replyEntity값 : " + reply));

        List<ReplyDTO> replyDTOList =
                replyList.stream().map(reply -> mapper.map(reply, ReplyDTO.class)  .setBoardDTO(   mapper.map(  reply.getBoard(), BoardDTO.class  ) ) )
                        .collect(Collectors.toList());



        replyDTOList.forEach(replyDTO -> log.info("replyDTO값 : " + replyDTO ));

        return replyDTOList;
    }

    @Override
    public ReplyDTO replyOne(Long rno) {

        Optional<Reply> replyOptional =  replyRepository.findById(rno);

        Reply  reply = replyOptional.get();

        ReplyDTO replyDTO = mapper.map(reply, ReplyDTO.class).setBoardDTO(mapper.map(reply.getBoard(), BoardDTO.class));

        return replyDTO;
    }


    //답변 수정기능
    //답변 번호 replyDTO에 이미 있음
    @Override
    public Long readModify(ReplyDTO replyDTO) {

        Optional<Reply>  replyOptional = replyRepository.findById(replyDTO.getRno());

        Reply reply =  replyOptional.get();

        reply.setRcontent(replyDTO.getRcontent());

        return  reply.getBoard().getBno();
    }

    @Override
    public void replyDelete(ReplyDTO replyDTO) {
        //답변 가져오기
        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getRno());

        //답변 존재할 때
        if(replyOptional.isPresent()) {

            Reply reply = replyOptional.get();

            replyRepository.delete(reply);
        }


    }

}
