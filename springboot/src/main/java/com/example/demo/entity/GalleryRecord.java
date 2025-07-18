package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

public class GalleryRecord {
    private Long id;
    private String title;
    private List<GalleryImage> images;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public GalleryRecord() {
    }

    public GalleryRecord(Long id, String title, List<GalleryImage> images, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.title = title;
        this.images = images;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

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

    public List<GalleryImage> getImages() {
        return images;
    }

    public void setImages(List<GalleryImage> images) {
        this.images = images;
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
} 