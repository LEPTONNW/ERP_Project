package com.example.demo.repository;

import com.example.demo.entity.EmployeEntity;
import com.example.demo.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeRepository extends JpaRepository<EmployeEntity, Long> {
    Optional<EmployeEntity> findByMnoMno(Long mno); //UserEntity의 FK값인 Mno로 조회
}
