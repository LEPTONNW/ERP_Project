package com.example.demo.service;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;


@Service
public interface BoardService {
    public Long register(@Valid BoardDTO boardDTO, Principal principal);

    public List<BoardDTO> selectAll();

    //size, page, keyword, types
    PageResponesDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    public BoardDTO read(Long bno);

    public Long modify(BoardDTO boardDTO);

    @Transactional
    void delete(Long bno);

    String list(Principal principal);

    public List<BoardDTO> boardDTOList();

    public List<BoardDTO> titlelike(String title);
    public List<BoardDTO> contentlike(String content);
    public List<BoardDTO> writerlike(String writer);




}