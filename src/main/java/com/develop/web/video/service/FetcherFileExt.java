package com.develop.web.video.service;

import org.springframework.stereotype.Service;

@Service
public class FetcherFileExt {
        /**
     * @description 업로드한 파일에서 확장자를 추출한다.
     * @param originalFilename 원본 파일 이름
     * @return 파일 확장자
     * */
    public String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
