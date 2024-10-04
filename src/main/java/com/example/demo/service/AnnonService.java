package com.example.demo.service;

import com.example.demo.dto.AnnonDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface AnnonService {

    public Long create(@Valid  AnnonDTO annonDTO, Principal principal) throws EntityNotFoundException;

    public List<AnnonDTO> selectAll();

    public void update(AnnonDTO annonDTO);

    //페이징 처리
    public PageResponesDTO<AnnonDTO> main(PageRequestDTO pageRequestDTO);

    String main(Principal principal);

    public AnnonDTO load(Long bno);

    public Long alter(AnnonDTO annonDTO);

    public List<AnnonDTO> getallAnnon();

    void delete(Long bno);

    public List<AnnonDTO> titlelike(String title);
    public List<AnnonDTO> contentlike(String content);
    public List<AnnonDTO> writerlike(String writer);

}
