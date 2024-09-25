package com.example.demo.repository.search;

import com.example.demo.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    //제목검색 , 내용검색, 제목+내용검색
    //페이징처리까지

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);


}
