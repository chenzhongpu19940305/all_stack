package com.tool.dto;

/**
 * 搜索请求对象
 * 
 * @author tool-service
 * @version 1.0.0
 */
public class SearchRequest {
    
    private String query;
    
    public SearchRequest() {}
    
    public SearchRequest(String query) {
        this.query = query;
    }
    
    // Getters and Setters
    public String getQuery() {
        return query;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }
} 