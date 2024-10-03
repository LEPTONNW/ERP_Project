package com.example.demo.service;

import com.example.demo.dto.EmployeDTO;
import com.example.demo.dto.EmployeMergeDTO;
import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.EmployeEntity;
import com.example.demo.entity.UsersEntity;
import com.example.demo.repository.EmployeRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class EmployeServiceImpl implements EmployeService{

    private final EmployeRepository employeRepository;
    private final UserRepository userRepository;
    private ModelMapper modelMapper = new ModelMapper();

    //연결된 Mno(FK)값으로 해당하는 유저의 EmployeEntity 정보를 가져옴
    public EmployeDTO getemp(Long mno) {
        // Optional에서 값을 안전하게 꺼내기 위해 orElse를 사용하여 null 체크
        Optional<EmployeEntity> employeEntity = employeRepository.findByMnoMno(mno);

        if (employeEntity.isPresent()) {
            // 값이 있을 경우 DTO로 매핑
            EmployeEntity employeEntity2 = employeEntity.get();
            return modelMapper.map(employeEntity2, EmployeDTO.class);
        } else {
            // 값이 없을 경우 빈 DTO 반환
            EmployeDTO employeDTO = new EmployeDTO();
            employeDTO.setEno(0L);
            employeDTO.setJob("");
            employeDTO.setSal(0);
            employeDTO.setRank("");
            employeDTO.setMno(0L);
            employeDTO.setJoin_date(LocalDate.parse("1999-01-01"));
            return employeDTO;
        }
    }

    public EmployeDTO setemp(Long mno, EmployeDTO employeDTO) {

        //mno로 EmployeEntity 정보 가져오기
        Optional<EmployeEntity> employeEntity_op = employeRepository.findByMnoMno(mno);

        EmployeEntity employeEntity1;
        if (employeEntity_op.isPresent()) {
            // 기존 엔티티가 존재할 경우 이때 Eno값도 여기에 저장되어짐 그래서 업데이트 되는것
            employeEntity1 = employeEntity_op.get();

            // 기존 엔티티의 필드 업데이트
            employeEntity1.setJob(employeDTO.getJob());
            employeEntity1.setRank(employeDTO.getRank());
            employeEntity1.setJoin_date(employeDTO.getJoin_date());
            employeEntity1.setSal(employeDTO.getSal());
        }
        else {
            // 엔티티가 존재하지 않을 경우 새로운 엔티티 생성
            //mno로 유저 엔티티 정보 가져옴
            Optional<UsersEntity> usersEntity_Op = userRepository.findByMno(mno);
            UsersEntity usersEntity1 = usersEntity_Op.get();

            //엔티티를 DTO로 변환
            UsersDTO usersDTO = modelMapper.map(usersEntity1, UsersDTO.class);

            //Mno값 지정
            employeDTO.setMno(usersDTO.getMno());

            //employeDTO를 Entity 로 변환
            employeEntity1 = modelMapper.map(employeDTO, EmployeEntity.class);

            //이후코드에서는 Eno값이 없기때문에 새로생성되어 저장되는것
        }

        // 데이터베이스에 저장
        EmployeEntity savedEntity = employeRepository.save(employeEntity1);

        // 저장된 EmployeEntity를 DTO로 변환하여 반환
        return modelMapper.map(savedEntity, EmployeDTO.class);

    }
}
