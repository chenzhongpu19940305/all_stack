package com.tool.dto;

/**
 * 视频文件DTO
 */
public class VideoFileDTO {
    private Long id;
    private String videoData;
    private String name;
    private Long size;
    private String type;

    // 构造函数
    public VideoFileDTO() {}

    public VideoFileDTO(String videoData, String name) {
        this.videoData = videoData;
        this.name = name;
    }

    public VideoFileDTO(String videoData, String name, Long size, String type) {
        this.videoData = videoData;
        this.name = name;
        this.size = size;
        this.type = type;
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoData() {
        return videoData;
    }

    public void setVideoData(String videoData) {
        this.videoData = videoData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "VideoFileDTO{" +
                "id=" + id +
                ", videoData='" + videoData + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                '}';
    }
} 