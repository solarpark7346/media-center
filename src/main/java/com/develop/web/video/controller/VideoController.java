package com.develop.web.video.controller;
import com.develop.web.video.service.FetcherFileExt;
import com.develop.web.video.service.MakeNameUUID;
import com.develop.web.video.service.MediaDataFetcher;
import com.develop.web.video.service.UploadFile;
import com.develop.web.video.dto.Metadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

/* http://localhost:8081/swagger-ui */

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class VideoController {

    private final FetcherFileExt fetcherFileExt;
    private final MakeNameUUID makeNameUUID;
    private final UploadFile uploadFile;
    private final MediaDataFetcher mediaDataFetcher;

    @Value("/opt/homebrew/Cellar/ffmpeg/5.1.2_6/bin/ffmpeg")
    private String ffmpegPath;
    @Value("/opt/homebrew/Cellar/ffmpeg/5.1.2_6/bin/ffprobe")
    private String ffprobePath;

    private FFmpeg ffmpeg;
    private FFprobe ffprobe;

    /**
    * FFmpeg를 사용할 수 있도록 초기화
    * */
    @PostConstruct
    public void init(){
        try {
            ffmpeg = new FFmpeg(ffmpegPath);
            Assert.isTrue(ffmpeg.isFFmpeg());

            ffprobe = new FFprobe(ffprobePath);
            Assert.isTrue(ffprobe.isFFprobe());

            log.debug("VideoFileUtils init complete.");
        } catch (Exception e) {
            log.error("VideoFileUtils init fail.", e);
        }
    }

    @Value("${app.upload.dir:${user.home}/movies/archive}")
    private String uploadDir;

    @GetMapping(value = "/upload")
   public ResponseEntity<Metadata> upload(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        String fileOriginalFilename = file.getOriginalFilename();
        assert fileOriginalFilename != null;

        String extractExt = fetcherFileExt.extractExt(fileOriginalFilename);
        String uuid = makeNameUUID.createStoreFileName(extractExt);

        String source = uploadFile.copyFile(file, uuid, uploadDir);
        Metadata metadata = mediaDataFetcher.getMediaInfo(ffprobe, uploadDir+"/"+source);

        return ResponseEntity.ok().body(metadata);
    }
}