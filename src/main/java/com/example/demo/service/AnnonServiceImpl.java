package com.example.demo.service;

import com.example.demo.dto.AnnonDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import com.example.demo.entity.Annon;
import com.example.demo.repository.AnnonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class AnnonServiceImpl implements AnnonService {

    private final AnnonRepository annonRepository;

    private ModelMapper mapper = new ModelMapper();

    @Override
    public Long create(@Valid AnnonDTO annonDTO) {
        log.info("서비스로 들어온 dto: " + annonDTO);
        Annon annon = mapper.map(annonDTO, Annon.class);
        log.info("서비스에서 변환된 dto > entity : " + annon);
        annonRepository.save(annon);



        return null;
    }

    @Override
    public List<AnnonDTO> selectAll() {
        List<Annon> annonList = annonRepository.findAll();

        List<AnnonDTO> annonDTOList =
                annonList.stream().map(abc -> mapper.map(abc, AnnonDTO.class)).collect(Collectors.toList());

        return annonDTOList;
    }

    @Override
    public void update(AnnonDTO annonDTO) {
        //트랜잭션이 적용이 되어있어서 엔티티매니저가 적용된다.

        Annon annon = annonRepository
                .findById(annonDTO.getBno())
                .orElseThrow(EntityNotFoundException::new);

        annon.setContent(annonDTO.getContent());

    }

    @Override
    public PageResponesDTO<AnnonDTO> main(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<Annon> annonpage = annonRepository.searchAll(types, keyword, pageable);

        //보드타입의 리스트가 >>> 보드DTO 타입의 리스트로 변환
        // 보드 리스트가 null일 경우 빈 리스트로 처리
        List<AnnonDTO> annonDTOList = annonpage.getContent() == null ?
                Collections.emptyList() :
                annonpage.getContent()
                        .stream()
                        .map(annon -> mapper.map(annon, AnnonDTO.class))
                        .collect(Collectors.toList());

        //반환값 처리
        return PageResponesDTO.<AnnonDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(annonDTOList)
                .total((int)annonpage.getTotalElements())
                .build();
    }

    @Override
    public AnnonDTO load(Long bno) {



        log.info("LOG!!! :");
        Annon annon = annonRepository.findById(bno)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 엔티티를 DTO로 변환하여 반환
        //log.info("LOG!!! : {}", mapper.map(annon, AnnonDTO.class));
        AnnonDTO annonDTO = mapper.map(annon, AnnonDTO.class);
        return annonDTO;
    }

    @Override
    public Long alter(AnnonDTO annonDTO) {

        Annon annon = annonRepository
                .findById(annonDTO.getBno())
                .orElseThrow(EntityNotFoundException::new);

        annon.setContent(annonDTO.getContent());
        annon.setTitle(annonDTO.getTitle());

        return annonDTO.getBno();
    }

    public Long delete(AnnonDTO annonDTO) {
        // bno로 게시물 찾기
        Optional<Annon> annon = annonRepository.findById(annonDTO.getBno());

        // 게시물이 존재하는 경우 삭제
        if (annon.isPresent()) {
            annonRepository.delete(annon.get()); // 삭제 수행
            return annonDTO.getBno();            // 삭제된 bno 반환
        } else {
            throw new EntityNotFoundException("공지사항이 존재하지 않습니다. : " + annonDTO.getBno());
        }
    }



}


