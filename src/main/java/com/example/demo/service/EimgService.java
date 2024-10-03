package com.example.demo.service;

import com.example.demo.dto.EimgDTO;
import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.EimgEntity;
import com.example.demo.entity.UsersEntity;
import com.example.demo.repository.EimgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
public class EimgService {
    private final EfileService efileService;
    private final EimgRepository eimgRepository;
    private ModelMapper mapper = new ModelMapper();

    public void eimgregister2(UsersEntity usersEntity, MultipartFile multipartFile, String employeImgLocation) {
        UUID uuid = UUID.randomUUID();
        String originalFilename = multipartFile.getOriginalFilename();
        String newSaveName = originalFilename
                .substring(originalFilename.lastIndexOf("/") + 1);
        String fileUploadFullUrl = employeImgLocation + "/" + uuid.toString() + "_" + newSaveName;

        efileService.uploadFile(multipartFile, fileUploadFullUrl);
        EimgEntity eimgEntity = new EimgEntity();
        eimgEntity.setUsersEntity(usersEntity);
        eimgEntity.setPicname(uuid.toString() + "_" + newSaveName);
        eimgEntity.setPic_url(employeImgLocation);
        eimgEntity.setOripicname(newSaveName);

        eimgRepository.save(eimgEntity);


    }
    public EimgDTO read(Long mno){
        EimgEntity eimgEntity = eimgRepository.findPK(mno);
        log.info("여기 달린 이미지에요 엔튀리"+eimgEntity);
        if(eimgEntity != null){
            EimgDTO eimgDTO = mapper.map(eimgEntity, EimgDTO.class);
            log.info("여기 달린 이미지에요 디티오"+eimgDTO);
            return  eimgDTO;
        }else {
            return  null;
        }


    }
    public List<EimgDTO> allread(){

        List<EimgEntity> eimgEntityList = eimgRepository.findallall();
        log.info("여기 달린 이미지에요 엔튀리"+eimgEntityList);

        List<EimgDTO> eimgDTOList =
                eimgEntityList.stream().map(eimgEntity -> mapper.map(eimgEntity, EimgDTO.class).setUserSDTO(mapper.map(eimgEntity.getUsersEntity(), UsersDTO.class))).collect(Collectors.toList());
        //
        return eimgDTOList;
    }

}
