package com.tool.entity;

import java.time.LocalDateTime;

/**
 * 代码片段标签实体类
 * 
 * @author tool-service
 * @version 1.0.0
 */
public class CodeSnippetTag {
    
    private Long id;
    private Long recordId;
    private String tagName;
    private LocalDateTime createdAt;
    
    public CodeSnippetTag() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getRecordId() {
        return recordId;
    }
    
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
    
    public String getTagName() {
        return tagName;
    }
    
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}