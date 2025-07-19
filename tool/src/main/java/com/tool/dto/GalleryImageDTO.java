package com.tool.dto;

import java.time.LocalDateTime;

/**
 * Gallery图片数据传输对象
 * 
 * @author tool-service
 * @version 1.0.0
 */
public class GalleryImageDTO {
    
    private Long id;
    private String name;
    private String imageData;
    private Long fileSize;
    private String contentType;
    private LocalDateTime createdAt;
    
    public GalleryImageDTO() {}
    
    public GalleryImageDTO(Long id, String name, String imageData) {
        this.id = id;
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
} 