package com.example.demo.service;


import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import com.example.demo.entity.Board;
import com.example.demo.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Collections;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private ModelMapper mapper = new ModelMapper();

    @Override
    public Long register(BoardDTO boardDTO) {
        log.info("서비스로 들어온 dto: " + boardDTO);
        //등록
        Board board = mapper.map(boardDTO, Board.class);
        log.info("서비스에서 변환된 dto > entity : " + board);
        boardRepository.save(board);



        return null;
    }

    @Override
    public List<BoardDTO> selectAll() {
        List<Board> boardList = boardRepository.findAll();

        List<BoardDTO> boardDTOList =
                boardList.stream().map(abc -> mapper.map(abc, BoardDTO.class)).collect(Collectors.toList());

        return boardDTOList;
    }

    @Override
    public void update(BoardDTO boardDTO) {
        //트랜잭션이 적용이 되어있어서 엔티티매니저가 적용된다.

        Board board = boardRepository
                .findById(boardDTO.getBno())
                .orElseThrow(EntityNotFoundException::new);

        board.setContent(boardDTO.getContent());

    }

    //size, page, keyword, types
    @Override
    public PageResponesDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();

        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<Board> boardpage = boardRepository.searchAll(types, keyword, pageable);

        //보드타입의 리스트가 >>> 보드DTO 타입의 리스트로 변환
        // 보드 리스트가 null일 경우 빈 리스트로 처리
        List<BoardDTO> boardDTOList = boardpage.getContent() == null ?
                Collections.emptyList() :
                boardpage.getContent()
                        .stream()
                        .map(board -> mapper.map(board, BoardDTO.class))
                        .collect(Collectors.toList());


        //반환값 처리
        return PageResponesDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(boardDTOList)
                .total((int)boardpage.getTotalElements())
                .build();
    }

    @Override
    public BoardDTO read(Long bno) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 엔티티를 DTO로 변환하여 반환
        return mapper.map(board, BoardDTO.class);
    }


    //정보 수정
    @Override
    public Long modify(BoardDTO boardDTO) {

        Board board = boardRepository
                .findById(boardDTO.getBno())
                .orElseThrow(EntityNotFoundException::new);

        board.setContent(boardDTO.getContent());

        return boardDTO.getBno();
    }




}
