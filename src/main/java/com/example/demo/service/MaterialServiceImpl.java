package com.example.demo.service;

import com.example.demo.dto.MaterialDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import com.example.demo.entity.MaterialEntity;
import com.example.demo.repository.MaterialRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
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

        MaterialEntity materialEntity = materialRepository.findById(materialDTO.getMno())
                .orElseThrow(EntityNotFoundException::new);
        materialEntity.setContent(materialDTO.getContent());

    }

    @Override
    public void delete(Long mno) {
        materialRepository.deleteById(mno);

    }

    @Override
    public PageResponesDTO<MaterialDTO> list(PageRequestDTO pageRequestDTO) {
        return null;
    }
}
