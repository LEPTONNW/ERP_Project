package com.example.demo.repository;

import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.UsersEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<UsersEntity, Long> {

    Optional<UsersEntity> findByUserid(String userid); //아이디로 조회

    List<UsersEntity> findByUseridLike(String userid); //아이디로 조회
    // 'leptonw'
    // "%"leptonw"%"

    List<UsersEntity> findByEmailLike(String email); //이메일로 조회

    List<UsersEntity> findByB2bnumberLike(String b2bnumber); //사업자번호로 조회

    boolean existsByEmail(String email); //이메일 중복확인용

    Optional<UsersEntity> findByMno(Long mno); //mno로 조회
}
