package com.develop.web.video.service;

import com.develop.web.video.dto.FileDto;
import com.develop.web.video.dto.Metadata;
import com.develop.web.video.dto.SendMessageDto;
import com.develop.web.video.mapper.IngestMapper;
import com.develop.web.websocket.MyWebSocketClient;
import com.develop.web.utils.VideoFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class Convert {
  private final VideoFileUtils videoFileUtils;
  private final IngestMapper ingestMapper;
  private final MediaDataFetcher mediaDataFetcher;

  public Metadata transcoding(Integer ingestId, String filePath, String outputPath, FileDto fileDto) throws IOException {

    FFmpegBuilder builder = new FFmpegBuilder().setInput(filePath)
            .overrideOutputFiles(true)
            .addOutput(outputPath)
            .disableSubtitle()
            .setVideoResolution(1280, 720)
            .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
            .done();

    FFmpegExecutor executor = new FFmpegExecutor(videoFileUtils.ffmpeg, videoFileUtils.ffprobe);
    FFmpegProbeResult probeResult = videoFileUtils.ffprobe.probe(filePath);

    CompletableFuture<Metadata> completableFuture = CompletableFuture.supplyAsync(() -> {
      executor.createJob(builder, progress -> {
        double percentage = Math.round(progress.out_time_ns / probeResult.getFormat().duration) / 10000000.0;

        String sendPercentage = String.valueOf(String.format("%.2f", percentage));
        String sendIngestId = String.valueOf(ingestId);

        try {
          SendMessageDto sendMessageDto = new SendMessageDto();
          sendMessageDto.setIngestId(sendIngestId);
          sendMessageDto.setPercentage(sendPercentage);

          MyWebSocketClient.sendMessageToAll(sendMessageDto);
          System.out.println(percentage);

        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }).run();
      ingestMapper.insertIngestSuccessRequest(ingestId);
      log.info("ingestId:" + ingestId + "등록");

      try {
        return mediaDataFetcher.getMediaInfo(videoFileUtils.ffprobe, outputPath, fileDto);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    });

    completableFuture.thenApply((Void) -> {
      Metadata convertMetadata;

      try {
        convertMetadata = mediaDataFetcher.getMediaInfo(videoFileUtils.ffprobe, outputPath, fileDto);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      return convertMetadata;
    });

    return completableFuture.join();
  }
}
