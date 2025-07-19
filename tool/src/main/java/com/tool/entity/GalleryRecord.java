package com.tool.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AI问答记录实体类
 * 
 * @author tool-service
 * @version 1.0.0
 */
public class GalleryRecord {
    
    private Long id;
    
    private String title;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private List<GalleryImage> images;
    
    public GalleryRecord() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public GalleryRecord(String title) {
        this();
        this.title = title;
    }
    
    // Getters and Setters
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
    
    public List<GalleryImage> getImages() {
        return images;
    }
    
    public void setImages(List<GalleryImage> images) {
        this.images = images;
    }
} 