package com.example.demo.repository;

import com.example.demo.entity.Board;
import com.example.demo.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

}
