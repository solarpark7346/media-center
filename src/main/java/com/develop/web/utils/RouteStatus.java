package com.develop.web.utils;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class RouteStatus {

    public void uploadPathCheck(String uploadDir){

        File dir = new File(uploadDir);

        if(!dir.exists()) {
          throw new NullPointerException("해당 경로에 폴더가 존재하지 않습니다.");
        }
    }
}
