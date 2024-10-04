package com.example.demo.service;


import com.example.demo.dto.EimgDTO;
import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.BimgEntity;
import com.example.demo.entity.EmployeEntity;
import com.example.demo.entity.UsersEntity;
import com.example.demo.repository.BimgRepository;
import com.example.demo.repository.EimgRepository;
import com.example.demo.repository.EmployeRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class UserServiceImpl implements UserService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Value("${employeImgLocation}")
    private String employeImgLocation;
    private final EimgRepository eimgRepository;
    private final EimgService eimgService;
    private final EmployeRepository employeRepository;
    private final BimgRepository bimgRepository;



    //회원가입 처리
    @Override
    public Long register(UsersDTO usersDTO) {
        //log.info(usersDTO);

        //패스워드 암호화
        String encrypt = passwordEncoder.encode(usersDTO.getPass());
        usersDTO.setPass(encrypt);

        //DTO를 엔티티로 변환
        UsersEntity usersEntity = modelMapper.map(usersDTO, UsersEntity.class);
        //log.info(usersEntity);

        //데이터베이스에 저장
        UsersEntity savedUser = userRepository.save(usersEntity);

        //저장된 엔티티의 ID 반환
        return savedUser.getMno();
    }
    @Override
    public boolean register2(UsersDTO usersDTO, MultipartFile multipartFile){
        log.info("dto들어옴"+usersDTO);

        Optional<UsersEntity> usersEntity =  userRepository.findByUserid(usersDTO.getUserid());

        UsersEntity usersEntity2 = usersEntity.get();
        if(usersEntity == null){
            usersEntity2 = modelMapper.map(usersDTO, UsersEntity.class);
            userRepository.save(usersEntity2);
            eimgService.eimgregister2(usersEntity2, multipartFile ,employeImgLocation );
            log.info("여기는 레지 진행완");
            return true;
        }else {
            log.info("저장 불가중복");
            return false;
        }
    }
    //중복확인
    @Override
    public boolean checkIfUserExsists(String userid) {
        return userRepository.findByUserid(userid.trim()).isPresent();
    }

    @Override
    public boolean checkUserEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UsersDTO getUser(String userid) {
        Optional<UsersEntity> usersEntity = userRepository.findByUserid(userid);

        //엔티티를 DTO로 변환하여 리턴
        return usersEntity.map(entity -> modelMapper.map(entity, UsersDTO.class))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UsersDTO updateUser(String userid, UsersDTO usersDTO, MultipartFile multipartFile) {
        Optional<UsersEntity> usersEntity = userRepository.findByUserid(userid);

        if(usersEntity.isPresent()) {
            UsersEntity usersEntity1 = usersEntity.get();

            //사용자 정보 업데이트
            //개인정보
            usersEntity1.setName(usersDTO.getName());
            usersEntity1.setAge(usersDTO.getAge());
            usersEntity1.setGender(usersDTO.getGender());
            usersEntity1.setEmail(usersDTO.getEmail());
            usersEntity1.setPhone(usersDTO.getPhone());

            //회사정보
            usersEntity1.setB2bname(usersDTO.getB2bname());
            usersEntity1.setB2baddr(usersDTO.getB2baddr());
            usersEntity1.setB2bexpont(usersDTO.getB2bexpont());
            usersEntity1.setB2bemail(usersDTO.getB2bemail());
            usersEntity1.setB2bcontact(usersDTO.getB2bcontact());
            usersEntity1.setB2bnumber(usersDTO.getB2bnumber());

            //권한
            usersEntity1.setPermission(usersDTO.getPermission());

            //업데이트된 엔티티를 저장
            userRepository.save(usersEntity1);
            if(multipartFile != null){
                eimgService.eimgregister2(usersEntity1 , multipartFile, employeImgLocation);
            }
            return modelMapper.map(usersEntity1, UsersDTO.class);

        }
        else {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }


    //패스워드변경
    @Override
    public UsersDTO updatePass(String userid, UsersDTO usersDTO) {
        Optional<UsersEntity> usersEntity = userRepository.findByUserid(userid);

        if(usersEntity.isPresent()) {
            UsersEntity usersEntity1 = usersEntity.get();

            //암호화
            String encrypt = passwordEncoder.encode(usersDTO.getPass());

            //패스워드변경
            usersEntity1.setPass(encrypt);

            //저장
            userRepository.save(usersEntity1);

            //DTO로 변환하여 리턴
            return modelMapper.map(usersEntity1, UsersDTO.class);
        }

        else {
            throw new UsernameNotFoundException("사용자 찾을 수 없음, 잘못된 접근입니다.");
        }
    }

    //발급된 임시비밀번호로 패스워드 변경
    @Override
    public String forgotpass(String userid, String pass) {
        Optional<UsersEntity> usersEntity = userRepository.findByUserid(userid);

        if(usersEntity.isPresent()) {
            UsersEntity usersEntity1 = usersEntity.get();

            //암호화
            String encrypt = passwordEncoder.encode(pass);

            //패스워드 변경
            usersEntity1.setPass(encrypt);

            //저장
            userRepository.save(usersEntity1);

            return modelMapper.map(usersEntity1, String.class);
        }
        else  {
            throw new UsernameNotFoundException("사용자를 찾을 수 없음, 잘못된 접근입니다.");
        }
    }

    //모든 유저 가져오기 기능
    @Override
    public List<UsersDTO> getAllUser() {
        //List에 모든 유저정보 담아냄
        List<UsersEntity> usersEntities = userRepository.findAll();

        //매퍼와 Collectors 이용해서 DTO로 변환하여 반환
        return usersEntities.stream()
                .map(user -> modelMapper.map(user, UsersDTO.class))
                .collect(Collectors.toList());
    }

    //아이디로 검색
    @Override
    public List<UsersDTO> getIdUser(String userid) {
        List<UsersEntity> usersEntities = userRepository.findByUseridLike(userid);

        //엔티티를 Collectors DTO로 변환하여 리턴
        return usersEntities.stream()
                .map(user -> modelMapper.map(user, UsersDTO.class))
                .collect(Collectors.toList());
    }

    //관리자페이지 이메일로 검색
    @Override
    public List<UsersDTO> getEaUser(String email) {
        List<UsersEntity> usersEntities = userRepository.findByEmailLike(email);

        //엔티티를 Collectors DTO로 변환하여 리턴
        return usersEntities.stream()
                .map(user -> modelMapper.map(user, UsersDTO.class))
                .collect(Collectors.toList());
    }

    //관리자 페이지 사업자번호로 검색
    @Override
    public List<UsersDTO> getB2User(String b2bnumber) {
        List<UsersEntity> usersEntities = userRepository.findByB2bnumberLike(b2bnumber);

        //엔티티를 Collectors DTO로 변환하여 리턴
        return usersEntities.stream()
                .map(user -> modelMapper.map(user, UsersDTO.class))
                .collect(Collectors.toList());
    }


    //유저정보 삭제
    @Override
    public String delete_user(String userid) {
        Optional<UsersEntity> usersEntity = userRepository.findByUserid(userid);
        UsersEntity usersEntity1 = usersEntity.get();

        Optional<EmployeEntity> employeEntity = employeRepository.findByMnoMno(usersEntity1.getMno());
        Optional<BimgEntity> bimgEntity = bimgRepository.findById(usersEntity1.getMno());


        if(usersEntity.isPresent()) {
            if(employeEntity.isPresent()) {
                //모든 데이터가 있을 경우 삭제
                if(bimgEntity.isPresent()) {
                    employeRepository.delete(employeEntity.get());
                    userRepository.delete(usersEntity.get());
                    bimgRepository.delete(bimgEntity.get());
                    return "사용자가 정상적으로 삭제되었습니다.";
                }

                //유저와 사원 데이터만 있을 경우 삭제
                else {
                    userRepository.delete(usersEntity.get());
                    employeRepository.delete(employeEntity.get());
                    return "사용자가 정상적으로 삭제되었습니다.";
                }

            }
            else {
                //사진과 유저데이터만 있을 경우 삭제
                if(bimgEntity.isPresent()) {
                    bimgRepository.delete(bimgEntity.get());
                    userRepository.delete(usersEntity.get());
                    return "사용자가 정상적으로 삭제되었습니다.";
                }
                else {
                    //유저 데이터만 있을 경우 삭제
                    userRepository.delete(usersEntity.get());
                    return "사용자가 정상적으로 삭제되었습니다.";
                }
            }
        }
        else {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.잘못된 사용자 입니다.");
        }

    }
}
