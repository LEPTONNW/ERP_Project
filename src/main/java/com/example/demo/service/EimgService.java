package com.example.demo.service;

import com.example.demo.dto.EimgDTO;
import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.BimgEntity;
import com.example.demo.entity.EimgEntity;
import com.example.demo.entity.UsersEntity;
import com.example.demo.repository.EimgRepository;
import com.example.demo.repository.EmployeRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
public class EimgService {
    private final EfileService efileService;
    private final EimgRepository eimgRepository;
    private final UserRepository userRepository;
    private ModelMapper mapper = new ModelMapper();

    public void eimgregister2(UsersEntity usersEntity, MultipartFile multipartFile, String employeImgLocation) {

        String directoryPath = "C:\\uploads\\employe";

        // File 객체 생성
        File directory = new File(directoryPath);

        // 폴더 존재 여부 확인 후 생성 시도
        if (!directory.exists()) {
            boolean created = directory.mkdirs();


        //사진을추가할 유저의 Mno값 받아옴
        Long delete_mno = usersEntity.getMno();
        //받은 Mno로 현재 등록되어있는 사진의 엔티티를 가져옴
        EimgEntity eimgEntity2 = eimgRepository.findByUsersEntityMno(delete_mno);
        if(eimgEntity2 != null) {
            String old_filename =  eimgEntity2.getPicname();

            //사진경로+이름 지정
            File file = new File("C:\\upload\\employe\\" + old_filename);

            if(file.exists()) {
                //해당경로의 파일 삭제
                boolean deleted = file.delete();

                //먼저있던 엔티티안의 레코드 삭제
                eimgRepository.delete(eimgEntity2);
            }
        }


        //랜덤으로 pic이름 정함
        UUID uuid = UUID.randomUUID();

        //업로드한 파일의 이름을 가져옴
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

    }
    public EimgDTO read(Long mno){
        //Optional<UsersEntity> usersEntity = userRepository.findByMno(mno);
        //UsersEntity usersEntity1 = usersEntity.get();

        //UsersDTO usersDTO = mapper.map(usersEntity1, UsersDTO.class);

        //Long u_mno = usersDTO.getMno();

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

}
