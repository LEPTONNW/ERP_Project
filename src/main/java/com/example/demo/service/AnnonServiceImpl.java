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

        // 게시물 검색
        Page<Annon> annonPage = annonRepository.searchAll(types, keyword, pageable);

        // Board 리스트를 AnnonDTO 리스트로 변환
        List<AnnonDTO> annonDTOList = annonPage.getContent() == null ?
                Collections.emptyList() :
                annonPage.getContent()
                        .stream()
                        .map(annon -> mapper.map(annon, AnnonDTO.class)) // 변환 타입 변경
                        .collect(Collectors.toList());

        // 반환값 처리
        return PageResponesDTO.<AnnonDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(annonDTOList)
                .total((int) annonPage.getTotalElements())
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

    @Transactional
    @Override
    public void delete(Long bno) {
        if (annonRepository.existsById(bno)) {
            annonRepository.deleteById(bno); // 게시글 삭제
            log.info("게시글이 성공적으로 삭제되었습니다. bno: {}", bno); // 삭제 성공 로깅
        } else {
            throw new EntityNotFoundException("공지사항이 존재하지 않습니다."); // 예외 처리
        }

    }

}

