package com.develop.web.video.service;

import com.develop.web.video.dto.Metadata;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service
public class MediaDataFetcher {
        /**
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
    public Metadata getMediaInfo(FFprobe ffprobe, String filePath) throws IOException {
        System.out.println("파일 : " + filePath);
        FFmpegProbeResult probeResult = ffprobe.probe(filePath);

        Metadata metadata = new Metadata();

        metadata.filename = StringUtils.getFilename(probeResult.getFormat().filename);
        metadata.width = probeResult.getStreams().get(0).width;
        metadata.height = probeResult.getStreams().get(0).height;
        metadata.format_name = probeResult.getFormat().format_name;
        metadata.format_long_name = probeResult.getFormat().format_long_name;
        metadata.tags = probeResult.getFormat().tags.toString();
        metadata.duration = probeResult.getFormat().duration;
        metadata.size = probeResult.getFormat().size;

        System.out.println(metadata.filename);

        return metadata;
    }
}
