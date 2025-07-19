package com.tool.dto;

/**
 * 搜索视频请求DTO
 */
public class SearchVideoRequest {
    private String query;
    private Integer page;
    private Integer size;

    // 构造函数
    public SearchVideoRequest() {}

    public SearchVideoRequest(String query) {
        this.query = query;
    }

    public SearchVideoRequest(String query, Integer page, Integer size) {
        this.query = query;
        this.page = page;
        this.size = size;
    }

    // Getter和Setter方法
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "SearchVideoRequest{" +
                "query='" + query + '\'' +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
} 