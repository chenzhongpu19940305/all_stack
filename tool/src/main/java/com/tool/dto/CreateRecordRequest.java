package com.tool.dto;

import java.util.List;

/**
 * 创建记录请求对象
 * 
 * @author tool-service
 * @version 1.0.0
 */
public class CreateRecordRequest {
    
    private String title;
    private String description;
    private String code;
    private String language;
    private List<GalleryImageDTO> images;
    
    public CreateRecordRequest() {}
    
    public CreateRecordRequest(String title, String description, List<GalleryImageDTO> images) {
        this.title = title;
        this.description = description;
        this.images = images;
    }
    
    // Getter和Setter方法
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
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public List<GalleryImageDTO> getImages() {
        return images;
    }
    
    public void setImages(List<GalleryImageDTO> images) {
        this.images = images;
    }
}