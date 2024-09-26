package com.example.demo.repository.search;

import com.example.demo.dto.MaterialDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Log4j2
public class MaterialSearchImpl extends QuerydslRepositorySupport implements MaterialSearch {
    public MaterialSearchImpl() { super(MaterialDTO.class);}

    @Override
    public Page<MaterialDTO> searchAll(String[] types, String keyword, Pageable pageable) {
        return null;
    }
}
