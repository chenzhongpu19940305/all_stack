package com.tool.dto;

import java.util.List;

/**
 * 创建代码片段请求DTO
 * 
 * @author tool-service
 * @version 1.0.0
 */
public class CreateCodeSnippetRequest {
    
    private String title;
    private String description;
    private String code;
    private String language;
    private List<String> tags;
    
    public CreateCodeSnippetRequest() {}
    
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
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}