package com.example.demo.service;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import com.example.demo.entity.Board;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface BoardService {
    public Long register(BoardDTO boardDTO);

    public List<BoardDTO> selectAll();

    public void update(BoardDTO boardDTO);

    public void delete(Long dno);

    //페이징처리, 검색처리 한 목록
    public PageResponesDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
}