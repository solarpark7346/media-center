package com.develop.web.video.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;


@Service
public class ConvertUploadFile {

    /**
    * @description 클라이언트에서 받은 미디어를 복사한다.
    * @param file 영상, uploadDir 업로드 경로
    * */
    public String copyFile(MultipartFile file, String filenameUUID, String uploadConvertDir) {

        Path copyOfLocation =
                Paths.get(uploadConvertDir + File.separator + StringUtils.cleanPath(Objects.requireNonNull(filenameUUID)));

        try {
            Files.copy(file.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not store file : " + file.getOriginalFilename());
        }

        return filenameUUID;
    }

}

