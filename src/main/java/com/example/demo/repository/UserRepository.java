package com.example.demo.repository;

import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.UsersEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<UsersEntity, Long> {

    Optional<UsersEntity> findByUserid(String userid);

    boolean existsByEmail(String email); //이메일 중복확인용
}
