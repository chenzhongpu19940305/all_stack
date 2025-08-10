package com.tool.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 代码片段记录实体类
 * 
 * @author tool-service
 * @version 1.0.0
 */
public class CodeSnippetRecord {
    
    private Long id;
    private String title;
    private String description;
    private String code;
    private String language;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CodeSnippetTag> tags;
    
    public CodeSnippetRecord() {}
    
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
    
    public List<CodeSnippetTag> getTags() {
        return tags;
    }
    
    public void setTags(List<CodeSnippetTag> tags) {
        this.tags = tags;
    }
}