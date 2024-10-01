package com.example.demo.repository;

import com.example.demo.entity.Reply;
import com.example.demo.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

}
