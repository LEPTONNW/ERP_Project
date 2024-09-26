package com.example.demo.service;

import com.example.demo.dto.MaterialDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;

import java.util.List;

public interface MaterialService {

    public void register(MaterialDTO materialDTO);
    public List<MaterialDTO> selectAll();
    public MaterialDTO read(Long mno);
    public void update(MaterialDTO materialDTO);
    public void delete(Long mno);
    public PageResponesDTO<MaterialDTO> list(PageRequestDTO pageRequestDTO);


}
