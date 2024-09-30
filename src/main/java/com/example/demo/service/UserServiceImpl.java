package com.example.demo.service;


import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.UsersEntity;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public UsersDTO updateUser(String userid, UsersDTO usersDTO) {
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
            usersEntity1.setB2bcontact(usersDTO.getName());
            usersEntity1.setB2bnumber(usersDTO.getB2bnumber());

            //업데이트된 엔티티를 저장
            userRepository.save(usersEntity1);

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

    //발급된 임시비밀번호로 패스워드 변경 (암호화X)
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

    @Override
    public List<UsersDTO> getAllUser() {
        //List에 모든 유저정보 담아냄
        List<UsersEntity> usersEntities = userRepository.findAll();

        //매퍼와 Collectors 이용해서 DTO로 변환하여 반환
        return usersEntities.stream()
                .map(user -> modelMapper.map(user, UsersDTO.class))
                .collect(Collectors.toList());
    }
}
