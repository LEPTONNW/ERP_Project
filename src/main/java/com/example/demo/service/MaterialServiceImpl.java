package com.example.demo.service;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.MaterialDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import com.example.demo.entity.Board;
import com.example.demo.entity.MaterialEntity;
import com.example.demo.repository.MaterialRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private ModelMapper mapper = new ModelMapper();


    @Override
    public void register(MaterialDTO materialDTO) {
        log.info("dto들어옴"+materialDTO);
        MaterialEntity materialEntity = mapper.map(materialDTO, MaterialEntity.class);
        materialRepository.save(materialEntity);

    }

    @Override
    public List<MaterialDTO> selectAll() {
        List<MaterialEntity> materialEntityList = materialRepository.findAll();

        List<MaterialDTO> materialDTOList=
                materialEntityList.stream().map(abc->mapper.map(abc, MaterialDTO.class)).collect(Collectors.toList());
        return materialDTOList;
    }

    @Override
    public MaterialDTO read(Long mno) {
        MaterialEntity materialEntity =
                materialRepository.findById(mno).orElseThrow(EntityNotFoundException::new);
        MaterialDTO materialDTO = mapper.map(materialEntity, MaterialDTO.class);
        return materialDTO;
    }

    @Override
    public void update(MaterialDTO materialDTO) {

        MaterialEntity materialEntity = materialRepository.findById(materialDTO.getNum())
                .orElseThrow(EntityNotFoundException::new);
        materialEntity.setContent(materialDTO.getContent());

    }

    @Override
    public void delete(Long mno) {
        materialRepository.deleteById(mno);

    }

    @Override
    public PageResponesDTO<MaterialDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        log.info("서비스에서 변환된 :  " + types);

        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("num");
        log.info(pageable.getSort());

        Page<MaterialDTO> materialDTOPage = materialRepository.searchAll(types, keyword, pageable);

        log.info("레포지토리에서 값은 서비스로 받았니?");


        return PageResponesDTO.<MaterialDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(materialDTOPage.getContent())
                .total(  (int)  materialDTOPage.getTotalElements())
                .build();
    }
}
