package com.example.demo.repository;

import com.example.demo.entity.MaterialEntity;
import com.example.demo.repository.search.MaterialSearch;
import com.example.demo.service.MaterialService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<MaterialEntity,Long>, MaterialSearch {
}
