package com.tool.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 视频记录DTO
 */
public class VideoRecordDTO {
    private Long id;
    private String title;
    private String description;
    private List<VideoFileDTO> videos;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 构造函数
    public VideoRecordDTO() {}

    public VideoRecordDTO(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<VideoFileDTO> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoFileDTO> videos) {
        this.videos = videos;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "VideoRecordDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", videos=" + videos +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 