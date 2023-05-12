package com.develop.web.video.service;

import com.develop.web.utils.VideoFileUtils;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Convert {
  private final VideoFileUtils videoFileUtils;

  @Async
  public void transcoding(String filePath, String outputPath) {
    FFmpegBuilder builder = new FFmpegBuilder().setInput(filePath)
            .overrideOutputFiles(true)
            .addOutput(outputPath)
            .disableSubtitle()
            .setVideoResolution(1280, 720)
            .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
            .done();

    FFmpegExecutor executor = new FFmpegExecutor(videoFileUtils.ffmpeg, videoFileUtils.ffprobe);
    executor.createJob(builder).run();
  }
}
