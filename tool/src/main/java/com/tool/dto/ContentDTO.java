package com.tool.dto;

import java.time.LocalDateTime;

/**
 * 内容数据传输对象
 */
public class ContentDTO {
    private Long id;
    private String title;
    private String keywords;
    private String description;
    private String videoPath;
    private String imagePath;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 构造函数
    public ContentDTO() {}

    public ContentDTO(Long id, String title, String keywords, String description, 
                     String videoPath, String imagePath, String content) {
        this.id = id;
        this.title = title;
        this.keywords = keywords;
        this.description = description;
        this.videoPath = videoPath;
        this.imagePath = imagePath;
        this.content = content;
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ContentDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", keywords='" + keywords + '\'' +
                ", description='" + description + '\'' +
                ", videoPath='" + videoPath + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
} 