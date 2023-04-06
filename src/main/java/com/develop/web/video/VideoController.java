package com.develop.web.video;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
/*
* http://localhost:8081/swagger-ui
* */
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @Value("${app.upload.dir:${user.home}/movies/archive}")
    private String uploadDir;

    @GetMapping(value = "/upload")
    public ResponseEntity<Metadata> upload(@RequestParam(value = "files", required = false) MultipartFile file) throws IOException {
        String source = videoService.uploadFile(file, uploadDir);
        Metadata metadata = videoService.getMediaInfo(uploadDir+"/"+source);

        return ResponseEntity.ok().body(metadata);
    }
}
