package com.develop.web.video.dto;

import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class SendMessageDto{
  public String percentage;
  public String ingestId;

  public String toJsonString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("percentage", percentage);
        jsonObject.addProperty("ingestId", ingestId);
        return jsonObject.toString();
    }
}
