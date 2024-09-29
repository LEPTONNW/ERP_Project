package com.example.demo.user;

import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.UsersEntity;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2

public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;
    private ModelMapper modelMapper = new ModelMapper();

    //로그인을 처리하는부분
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //아이디 없을시
        UsersEntity usersEntity = this.userRepository.findByUserid(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        UsersDTO usersDTO = modelMapper.map(usersEntity, UsersDTO.class); //엔티티를 DTO로 변환



        //log.info("LOG!!! : {}" ,usersEntity.toString());
        //log.info("LOG!!! : {}" , usersDTO.getPermission().toString().trim());
        //계정별 세션정보 부여
        List<GrantedAuthority> authorityList = new ArrayList<>();
        //SUPER_ADMIN

        if("SUPER_ADMIN".equals(usersDTO.getPermission().toString().trim())) {
            authorityList.add(new SimpleGrantedAuthority(UserRole.SUPER_ADMIN.getValue()));
        }

        //ADMIN
        else if("ADMIN".equals(usersDTO.getPermission().toString().trim())) {
            authorityList.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        }

        //USER
        else {
            authorityList.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }

        return new User(usersDTO.getUserid(), usersDTO.getPass(), authorityList);
    }
}
