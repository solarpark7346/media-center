package com.develop.web.video.service;

import com.develop.web.websocket.MyWebSocketClient;
import com.develop.web.utils.VideoFileUtils;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class Convert {
  private final VideoFileUtils videoFileUtils;

  @Async
  public void transcoding(String filePath, String outputPath) throws IOException {

    FFmpegBuilder builder = new FFmpegBuilder().setInput(filePath)
            .overrideOutputFiles(true)
            .addOutput(outputPath)
            .disableSubtitle()
            .setVideoResolution(1280, 720)
            .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
            .done();

    FFmpegExecutor executor = new FFmpegExecutor(videoFileUtils.ffmpeg, videoFileUtils.ffprobe);
    FFmpegProbeResult probeResult = videoFileUtils.ffprobe.probe(filePath);

    executor.createJob(builder, progress -> {
      double percentage = Math.round(progress.out_time_ns / probeResult.getFormat().duration) / 10000000.0;
          System.out.printf("진행률: %.2f\n", percentage);
      try {
        MyWebSocketClient.sendMessageToAll(String.valueOf(percentage));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }).run();
  }
}
