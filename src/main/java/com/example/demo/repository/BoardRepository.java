package com.example.demo.repository;

import com.example.demo.dto.BoardDTO;
import com.example.demo.entity.Board;
import com.example.demo.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {



    //제목으로 검색
    public List<Board> findByTitleLike(String title);
    //내용으로 검색
    public List<Board> findByContentLike(String content);
    //작성자로 검색
    public List<Board> findByWriterLike(String writer);
}
