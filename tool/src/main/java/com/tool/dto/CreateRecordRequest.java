package com.tool.dto;

import java.util.List;

/**
 * 创建Gallery记录的请求对象
 * 
 * @author tool-service
 * @version 1.0.0
 */
public class CreateRecordRequest {
    
    private String title;
    private String description;
    private List<GalleryImageDTO> images;
    
    public CreateRecordRequest() {}
    
    public CreateRecordRequest(String title, String description, List<GalleryImageDTO> images) {
        this.title = title;
        this.description = description;
        this.images = images;
    }
    
    // Getters and Setters
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
    
    public List<GalleryImageDTO> getImages() {
        return images;
    }
    
    public void setImages(List<GalleryImageDTO> images) {
        this.images = images;
    }
} 