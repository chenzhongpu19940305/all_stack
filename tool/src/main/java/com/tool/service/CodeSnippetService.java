package com.tool.service;

import com.tool.dto.ApiResponse;
import com.tool.dto.CreateCodeSnippetRequest;

/**
 * 代码片段服务接口
 * 
 * @author tool-service
 * @version 1.0.0
 */
public interface CodeSnippetService {
    
    /**
     * 获取代码片段记录列表
     * @param page 页码
     * @param size 每页大小
     * @param title 标题过滤
     * @param language 语言过滤
     * @return API响应
     */
    ApiResponse getRecords(Integer page, Integer size, String title, String language);
    
    /**
     * 搜索代码片段记录
     * @param query 搜索关键词
     * @param page 页码
     * @param size 每页大小
     * @return API响应
     */
    ApiResponse searchRecords(String query, Integer page, Integer size);
    
    /**
     * 创建代码片段记录
     * @param request 创建请求
     * @return API响应
     */
    ApiResponse createRecord(CreateCodeSnippetRequest request);
    
    /**
     * 更新代码片段记录
     * @param id 记录ID
     * @param request 更新请求
     * @return API响应
     */
    ApiResponse updateRecord(Long id, CreateCodeSnippetRequest request);
    
    /**
     * 删除代码片段记录
     * @param id 记录ID
     * @return API响应
     */
    ApiResponse deleteRecord(Long id);
    
    /**
     * 根据ID获取代码片段记录详情
     * @param id 记录ID
     * @return API响应
     */
    ApiResponse getRecordById(Long id);
    
    /**
     * 清理孤立标签
     * @return API响应
     */
    ApiResponse cleanOrphanTags();
    
    /**
     * 获取所有标签名
     * @return API响应
     */
    ApiResponse getAllTagNames();
}