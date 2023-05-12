package com.develop.web.utils;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class VideoFileUtils {
  @Value("/opt/homebrew/Cellar/ffmpeg/5.1.2_6/bin/ffmpeg")
  public String ffmpegPath;
  public FFmpeg ffmpeg;

  @Value("/opt/homebrew/Cellar/ffmpeg/5.1.2_6/bin/ffprobe")
  private String ffprobePath;
  public FFprobe ffprobe;

  @PostConstruct
  public void init() {
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
}