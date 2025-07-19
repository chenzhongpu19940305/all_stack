package com.tool.entity;

/**
 * 视频文件实体类
 */
public class VideoFile {
    private Long id;
    private Long recordId;
    private String videoData;
    private String name;
    private Long size;
    private String type;

    // 构造函数
    public VideoFile() {}

    public VideoFile(String videoData, String name) {
        this.videoData = videoData;
        this.name = name;
    }

    public VideoFile(String videoData, String name, Long size, String type) {
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

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    @Override
    public String toString() {
        return "VideoFile{" +
                "id=" + id +
                ", recordId=" + recordId +
                ", videoData='" + videoData + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                '}';
    }
} 