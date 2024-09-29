package com.example.demo.service;


import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.UsersEntity;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
