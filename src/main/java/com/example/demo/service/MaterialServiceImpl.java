package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.BimgEntity;
import com.example.demo.entity.Board;
import com.example.demo.entity.MaterialEntity;
import com.example.demo.entity.UsersEntity;
import com.example.demo.repository.BimgRepository;
import com.example.demo.repository.MaterialRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MaterialServiceImpl implements MaterialService {



    @Value("${materialImgLocation}")
    private String MaterialImgLocation;

    private final MaterialRepository materialRepository;
    private final BimgRepository bimgRepository;
    private ModelMapper mapper = new ModelMapper();
    private final BimgSerivce bimgSerivce;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public boolean register(MaterialDTO materialDTO, MultipartFile multipartFile, String userid) {

        Optional<UsersEntity> usersEntity = userRepository.findByUserid(userid);
        log.info("LOG!!! : "+ userid);
        UsersEntity usersEntity1 = usersEntity.get();


        log.info("dto들어옴"+materialDTO);

        MaterialEntity materialEntity = new MaterialEntity();
        if(materialDTO.getMatCode() != null) {
            materialEntity =  materialRepository.findByMatCode(materialDTO.getMatCode());
        }

        log.info("Mno들어옴"+usersEntity1.getMno());

        if(materialEntity == null){
            materialEntity = mapper.map(materialDTO, MaterialEntity.class);

            if(materialEntity.getMatBuyDate() != null) {
                materialRepository.save(materialEntity);
                bimgSerivce.Bimgregister(materialEntity, multipartFile, MaterialImgLocation);

                //글쓰는 유저의 Mno저장
                materialEntity.setMno(usersEntity1);
                log.info("여기는 레지 진행완");
                return true;
            }
            else {
                materialEntity.setMatBuyDate(LocalDate.parse("2000-01-01"));
                materialRepository.save(materialEntity);
                bimgSerivce.Bimgregister(materialEntity, multipartFile, MaterialImgLocation);

                //글쓰는 유저의 Mno저장
                materialEntity.setMno(usersEntity1);
                log.info("여기는 레지 진행완");
                return true;
            }

        }else {
            log.info("저장 불가중복");
            return false;
        }
    }





    @Override
    public List<MaterialDTO> selectAll() {
        List<MaterialEntity> materialEntityList = materialRepository.findAll();

        List<MaterialDTO> materialDTOList=
                materialEntityList.stream().map(abc->mapper.map(abc, MaterialDTO.class)).collect(Collectors.toList());
        return materialDTOList;
    }

    @Override
    public MaterialDTO read(Long num) {
        MaterialEntity materialEntity =
                materialRepository.findById(num).orElseThrow(EntityNotFoundException::new);
        MaterialDTO materialDTO = mapper.map(materialEntity, MaterialDTO.class);
        return materialDTO;
    }

    @Override
    public void update(MaterialDTO materialDTO , MultipartFile multipartFile , BimgDTO bimgDTO) {

        if (materialDTO.getNum() == null) {
            throw new IllegalArgumentException("아 혹시 이부분인가요?");
        }
        MaterialEntity materialEntity = materialRepository.findById(materialDTO.getNum())
                .orElseThrow(EntityNotFoundException::new);


        materialEntity.setMatName(materialDTO.getMatName());
        materialEntity.setMatCode(materialDTO.getMatCode());
        materialEntity.setMatAmount(materialDTO.getMatAmount());
        materialEntity.setMatPrice(materialDTO.getMatPrice());
        materialEntity.setMatBuyPlace(materialDTO.getMatBuyPlace());
        materialEntity.setMatBuyNum(materialDTO.getMatBuyNum());
        materialEntity.setMatText(materialDTO.getMatText());
        materialEntity.setMatBuyDate(materialDTO.getMatBuyDate());


        log.info(multipartFile.getOriginalFilename());
        log.info(multipartFile.getOriginalFilename());
        log.info(multipartFile.getOriginalFilename());
        log.info(multipartFile.getOriginalFilename());
        log.info(multipartFile.getOriginalFilename());
        if (bimgDTO.getBino() != null) {
            bimgSerivce.dele(bimgDTO.getBino());

            if(multipartFile != null && multipartFile.getSize() != 0){
                bimgSerivce.Bimgregister(materialEntity, multipartFile, MaterialImgLocation);
            }

        }else {
            if(multipartFile != null && multipartFile.getSize() != 0){
                bimgSerivce.Bimgregister(materialEntity, multipartFile, MaterialImgLocation);
            }

        }




    }

    @Transactional
    @Override
    public Long delete(Long num) {
        bimgRepository.deleteByMaterialEntity_Num(num);
        MaterialEntity materialEntity =  materialRepository.findById(num).get();

        if (materialEntity != null) {
            bimgRepository.deleteById(num);
            materialRepository.deleteById(num);

            log.info("혹시 서비스까진 오셨나요");
            return materialEntity.getNum();

        } else {
            return null;
        }

    }

    @Override
    public PageResponesDTO<MaterialDTO> list(PageRequestDTO pageRequestDTO) {


        String[] types = pageRequestDTO.getTypes();
        log.info("서비스에서 변환된 :  " + pageRequestDTO);

        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("num");
        log.info(pageable.getSort());

        Page<MaterialDTO> materialDTOPage = materialRepository.searchAll(types, keyword, pageable);


        return PageResponesDTO.<MaterialDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(materialDTOPage.getContent())
                .total(  (int)  materialDTOPage.getTotalElements())
                .build();
    }
}
