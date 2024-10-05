package com.example.demo.service;

import com.example.demo.dto.BimgDTO;
import com.example.demo.dto.MaterialDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponesDTO;
import com.example.demo.entity.BimgEntity;
import com.example.demo.entity.MaterialEntity;
import com.example.demo.repository.BimgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class BimgSerivce {
    private final FileService fileService;
    private final BimgRepository bimgRepository;
    private ModelMapper mapper = new ModelMapper();

    public void Bimgregister(MaterialEntity materialEntity, MultipartFile multipartFile, String materialImgLocation){



        UUID uuid = UUID.randomUUID();
        String originalFilename =multipartFile.getOriginalFilename();
        String newSaveName = originalFilename
                .substring( originalFilename.lastIndexOf("/")+1);
        String fileUploadFullUrl = materialImgLocation + "/" + uuid.toString()+"_"+newSaveName;

        fileService.uploadFile(multipartFile, fileUploadFullUrl);
        BimgEntity bimgEntity = new BimgEntity();
        bimgEntity.setMaterialEntity(materialEntity);
        bimgEntity.setImgname(uuid.toString()+"_"+newSaveName);
        bimgEntity.setImg_url(materialImgLocation);
        bimgEntity.setOriimgname(newSaveName);

        bimgRepository.save(bimgEntity);

    }
    public BimgDTO read(Long num){
        BimgEntity bimgEntity = bimgRepository.findpk(num);
        log.info("여기 달린 이미지에요 엔튀리"+bimgEntity);
        BimgDTO bimgDTO = mapper.map(bimgEntity, BimgDTO.class);
        log.info("여기 달린 이미지에요 디티오"+bimgDTO);
        return  bimgDTO;
    }
    public List<BimgDTO> allread(){
        //List<BimgEntity> bimgEntityList = bimgRepository.findAll();
        List<BimgEntity> bimgEntityList = bimgRepository.findallall();
        log.info("여기 달린 이미지에요 엔튀리"+bimgEntityList);

        List<BimgDTO> bimgDTOList =
                bimgEntityList.stream().map(bimgEntity -> mapper.map(bimgEntity, BimgDTO.class).setMaterialDTO(mapper.map(bimgEntity.getMaterialEntity(),MaterialDTO.class))).collect(Collectors.toList());
        //

        return bimgDTOList;
    }

    public void dele(Long num){
        BimgEntity bimgEntity = bimgRepository.findById(num).get();

        log.info(bimgEntity);
        String fullurl = bimgEntity.getImg_url() + File.separator + bimgEntity.getImgname();

        //물리적인 파일 삭제
        File file = new File(fullurl);
        if( file.exists() ){
            log.info("삭제함함함");

            //파일존재여부확인
            file.delete();
        }

        bimgRepository.deleteById(bimgEntity.getBino());
    }


}
