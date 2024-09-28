package com.example.demo.service;

import com.example.demo.dto.AnnonDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnnonService {

    public Long create(@Valid AnnonDTO annonDTO); //매인 -board register

    public List<AnnonDTO> selectAll();

    public void update(AnnonDTO annonDTO);

    //페이징 처리
    public PageResponesDTO<AnnonDTO> main(PageRequestDTO pageRequestDTO);



    public AnnonDTO load(Long bno);

    public Long alter(AnnonDTO annonDTO);

    public Long delete(AnnonDTO annonDTO);

}
