package com.example.demo.service;


import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.Board;
import com.example.demo.entity.UsersEntity;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final UserService userService;

    private final UserRepository userRepository;

    private ModelMapper mapper = new ModelMapper();

    @Override
    public Long register(BoardDTO boardDTO, Principal principal) {
        log.info("서비스로 들어온 dto: " + boardDTO);
        //등록
        UsersDTO usersDTO = userService.getUser(principal.getName());

        //UsersEntity usersEntity = mapper.map(usersDTO, UsersEntity.class);

        UsersEntity usersEntity = userRepository.findByUserid(principal.getName()).get();

        log.info("유저객체 : " +usersEntity);
        Board board = mapper.map(boardDTO, Board.class);
        board.setUsersEntity(usersEntity);
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
    public  String list(Principal principal) {
        String userId = principal.getName(); // 로그인한 사용자의 ID를 가져옴
        UsersDTO usersDTO = userService.getUser(userId); // 사용자 정보를 가져옴
        return userId;
    }

    //size, page, keyword, types
    @Override
    public PageResponesDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        // 사용자 정보를 가져오기

        // 게시물 페이지 조회
        Page<Board> boardPage = boardRepository.searchAll(types, keyword, pageable);

        // Board 리스트가 null일 경우 빈 리스트로 처리
        List<BoardDTO> boardDTOList = boardPage.hasContent() ? boardPage.getContent()
                .stream()
                .map(board -> {
                    BoardDTO boardDTO = mapper.map(board, BoardDTO.class);
                    if (board.getUsersEntity() != null) {
                        boardDTO.setUsersDTO(mapper.map(board.getUsersEntity(), UsersDTO.class));
                    }
                    return boardDTO;
                })
                .collect(Collectors.toList()) : Collections.emptyList();



        // 반환값 처리
        return PageResponesDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(boardDTOList)
                .total((int) boardPage.getTotalElements())
                .build();
    }

    @Override
    public BoardDTO read(Long bno) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        BoardDTO boardDTO = mapper.map(board, BoardDTO.class).setUsersDTO(mapper.map(board.getUsersEntity(), UsersDTO.class));

        // 엔티티를 DTO로 변환하여 반환
        return boardDTO;
    }


    //정보 수정
    @Override
    public Long modify(BoardDTO boardDTO) {

        Board board = boardRepository
                .findById(boardDTO.getBno())
                .orElseThrow(EntityNotFoundException::new);

        board.setContent(boardDTO.getContent());
        board.setTitle(boardDTO.getTitle());

        return boardDTO.getBno();
    }

    @Override
    @Transactional
    public void delete(Long bno) {
        if (boardRepository.existsById(bno)) {
            boardRepository.deleteById(bno); // 게시글 삭제
        } else {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다."); // 예외 처리
        }
    }

}


