package com.develop.web.video.controller;

import com.develop.web.utils.RouteStatus;
import com.develop.web.utils.VideoFileUtils;
import com.develop.web.video.dto.FileDto;
import com.develop.web.video.service.*;
import com.develop.web.video.dto.Metadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class VideoController {

  private final VideoFileUtils videoFileUtils;
  private final FetcherFileExt fetcherFileExt;
  private final UploadFile uploadFile;
  private final MediaDataFetcher mediaDataFetcher;
  private final RouteStatus routeStatus;
  private final Convert convert;

  @Value("${app.upload.dir:${user.home}/movies/mam/archive/}")
  private String uploadArchiveDir;

  @Value("${app.upload.dir:${user.home}/movies/mam/convert/}")
  private String uploadConvertDir;

  @Value("${app.upload.dir:${user.home}/movies/mam/convert/temp}")
  private String convertingTemp;

  @PostMapping(value = "/upload")
  public ResponseEntity<Metadata> upload(
    @RequestParam(value = "files", required = false)MultipartFile file,
    @RequestParam(value = "ingestId", required = false)Integer ingestId) throws IOException {

    LocalDate now = LocalDate.now();
    String ArchiveDirDate = uploadArchiveDir + now;
    String ConvertDirDate = uploadConvertDir + now;

    try {
      routeStatus.uploadPathCheck(ArchiveDirDate);
      routeStatus.uploadPathCheck(ConvertDirDate);
    } catch (NullPointerException e) {
      log.error(e.toString());
      log.info("해당 경로에 폴더를 생성하였습니다.");
      File dirA = new File(ArchiveDirDate);
      File dirC = new File(ConvertDirDate);
      dirA.mkdirs();
      dirC.mkdirs();
    }

    String fileOriginalFilename = file.getOriginalFilename();
    assert fileOriginalFilename != null;

    String extractExt = fetcherFileExt.extractExt(fileOriginalFilename);
    String uuid = UUID.randomUUID().toString();

    FileDto fileDto = new FileDto();
    fileDto.uuid = uuid;
    fileDto.originalFileName = fileOriginalFilename;
    fileDto.ext = extractExt;

    String filenameUUID = uuid + "." + extractExt;
    String filenameOriginal = fileOriginalFilename + "." + extractExt;

    String archiveSource = uploadFile.copyFile(file, filenameOriginal, ArchiveDirDate);
    String fileAPath = ArchiveDirDate + "/" + archiveSource;
    Metadata archiveMetadata = mediaDataFetcher.getMediaInfo(videoFileUtils.ffprobe, fileAPath, fileDto);
    System.out.println("\n▼ ArchiveResponseEntity ▼ " + archiveMetadata.toString());

    String convertSource = uploadFile.copyFile(file, filenameUUID, ConvertDirDate);
    String convertingSource = convertingTemp + "/" + convertSource;
    Metadata result = convert.transcoding(ingestId, fileAPath, convertingSource, fileDto);
    System.out.println("\n▼ ConvertResponseEntity ▼ " + result.toString());

    return ResponseEntity.ok().body(result);
  }
}
