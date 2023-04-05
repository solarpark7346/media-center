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

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    String filePath = "";
    String filename = "";

    @GetMapping(value = "/video")
    public ResponseEntity video() throws IOException {
        Metadata metadata = videoService.getMediaInfo(filePath + filename);

        return ResponseEntity.ok().body(metadata);
    }


}
