package com.develop.web.video;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
/*
* http://localhost:8081/swagger-ui
* */
public class VideoController {

    private final VideoFileUtils videoFileUtils;

    public VideoController(VideoFileUtils videoFileUtils) {
        this.videoFileUtils = videoFileUtils;
    }

    String filePath = "";
    String filename = "";

    @GetMapping(value = "/video")
    public ResponseEntity video() throws IOException {
        Metadata metadata = videoFileUtils.getMediaInfo(filePath + filename);

        return ResponseEntity.ok().body(metadata);
    }


}
