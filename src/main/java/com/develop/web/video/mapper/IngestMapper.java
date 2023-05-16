package com.develop.web.video.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IngestMapper {

  void insertIngestSuccessRequest(Integer ingestId);
}
