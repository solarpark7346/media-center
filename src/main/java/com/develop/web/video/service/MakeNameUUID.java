package com.develop.web.video.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MakeNameUUID {

    /**
    * @description 파일 이름이 이미 업로드된 파일들과 겹치지 않게 UUID를 사용
    * @param ext 확장명
    * @return 파일이름 + 확장자
    * */
    public String createStoreFileName(String ext) {
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
}
