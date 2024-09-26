package com.example.demo.repository.search;

import com.example.demo.dto.MaterialDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MaterialSearch {
    Page<MaterialDTO> searchAll(String[] types, String keyword, Pageable pageable);
}
