package com.develop.web.video.dto;

import lombok.Data;

@Data
public class Metadata {
    public String filename;
    public int width;
    public int height;
    public String format_name;
    public String format_long_name;
    public String tags;
    public double duration;
    public Long size;
}
