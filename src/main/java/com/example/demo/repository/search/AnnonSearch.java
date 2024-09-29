package com.example.demo.repository.search;

import com.example.demo.entity.Annon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnnonSearch {
    Page<Annon> searchAll(String[] types, String keyword, Pageable pageable);
}
