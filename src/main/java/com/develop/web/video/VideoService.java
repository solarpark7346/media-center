package com.develop.web.video;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class VideoService {
    @Value("/opt/homebrew/Cellar/ffmpeg/5.1.2_6/bin/ffmpeg")
    private String ffmpegPath;
    @Value("/opt/homebrew/Cellar/ffmpeg/5.1.2_6/bin/ffprobe")
    private String ffprobePath;

    private FFmpeg ffmpeg;
    private FFprobe ffprobe;

    /*
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

    /*
     * @description 미디어의 정보값을 보여준다.
     * @param filePath 파일 경로
     * @json data
     * - filename : 파일 이름
     * - width : 가로
     * - height : 세로
     * - format_name : 포멧 이름
     * - format_long_name : 포맷 전체 이름
     * - tags : 영상 태그 정보
     * - duration : 길이
     * - size : 사이즈(용량)
     * @return metadata (json)
     * */
    public Metadata getMediaInfo(String filePath) throws IOException {
        System.out.println("파일 : " + filePath);
        FFmpegProbeResult probeResult = ffprobe.probe(filePath);

        Metadata metadata = new Metadata();

        metadata.filename = probeResult.getFormat().filename;
        metadata.width = probeResult.getStreams().get(0).width;
        metadata.height = probeResult.getStreams().get(0).height;
        metadata.format_name = probeResult.getFormat().format_name;
        metadata.format_long_name = probeResult.getFormat().format_long_name;
        metadata.tags = probeResult.getFormat().tags.toString();
        metadata.duration = probeResult.getFormat().duration;
        metadata.size = probeResult.getFormat().size;

        return metadata;
    }

    /*
    * @description 파일 이름이 이미 업로드된 파일들과 겹치지 않게 UUID를 사용
    * @param originalFilename 원본 파일 이름
    * @return 파일이름.확장자
    * */
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    /*
     * @description 업로드한 파일에서 확장자를 추출한다.
     * @param originalFilename 원본 파일 이름
     * @return 파일 확장자
     * */
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}

