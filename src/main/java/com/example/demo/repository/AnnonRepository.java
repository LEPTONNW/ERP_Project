package com.example.demo.repository;

import com.example.demo.entity.Annon;
import com.example.demo.entity.Board;
import com.example.demo.entity.UsersEntity;
import com.example.demo.repository.search.AnnonSearch;
import com.example.demo.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnnonRepository extends JpaRepository<Annon, Long>, AnnonSearch {

    Optional<Annon> findById(Long bno);
}
