package com.tool.entity;

import java.time.LocalDateTime;

/**
 * 图片实体类
 * 
 * @author tool-service
 * @version 1.0.0
 */
public class GalleryImage {
    
    private Long id;
    private String name;
    private String imageData;
    private Long fileSize;
    private String contentType;
    private LocalDateTime createdAt;
    private Long recordId;
    
    public GalleryImage() {
        this.createdAt = LocalDateTime.now();
    }
    
    public GalleryImage(String name, String imageData) {
        this();
        this.name = name;
        this.imageData = imageData;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getImageData() {
        return imageData;
    }
    
    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Long getRecordId() {
        return recordId;
    }
    
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
} 