package com.example.demo.repository;

import com.example.demo.entity.EimgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EimgRepository extends JpaRepository<EimgEntity,Long> {

    @Query("select b from EimgEntity b where b.usersEntity.mno = :mno")
    EimgEntity findPK(Long mno);

    EimgEntity findByUsersEntityMno(Long mno);

    @Query("select a from EimgEntity a where a.usersEntity.mno is not null")
    List<EimgEntity> findallall();
    void deleteByUsersEntity_Mno(Long mno);

}
