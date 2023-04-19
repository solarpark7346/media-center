package com.develop.web.video.dto;

import lombok.Data;

@Data
public class Metadata {
    public String id;
    public String filename;
    public String ext;
    public int width;
    public int height;
    public String format_name;
    public String format_long_name;
    public String tags;
    public double duration;
    public Long size;

    public String toString(){
        return "\n[ Metadata ]\n" +
                "id = " + id + "\n" +
                "filename = " + filename + "\n" +
                "ext = " + ext + "\n" +
                "width = " + width + "\n" +
                "height = " + height + "\n" +
                "format_name = " + format_name + "\n" +
                "format_long_name = " + format_long_name + "\n" +
                "tags = " + tags + "\n" +
                "duration = " + duration + "\n" +
                "size = " + size + "\n";
    }
}
