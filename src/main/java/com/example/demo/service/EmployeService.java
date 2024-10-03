package com.example.demo.service;

import com.example.demo.dto.EmployeDTO;
import com.example.demo.dto.EmployeMergeDTO;
import com.example.demo.dto.UsersDTO;
import org.springframework.stereotype.Service;

@Service
public interface EmployeService {


    //연결된 Mno(FK)값으로 해당하는 유저의 EmployeEntity 정보를 가져옴
    EmployeDTO getemp(Long mno);

    EmployeDTO setemp(Long mno, EmployeDTO employeDTO);
}
