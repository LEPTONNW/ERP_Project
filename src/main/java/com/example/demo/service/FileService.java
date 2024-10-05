package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class FileService {
    public String uploadFile(MultipartFile multipartFile, String fileUploadFullUrl){
        Path path = Path.of(fileUploadFullUrl);//저장 여기다함
        try {
            multipartFile.transferTo(path);
        }catch (IOException ioException){
            System.out.println("사진이 이상해");
        }
        return null;
    }
}
