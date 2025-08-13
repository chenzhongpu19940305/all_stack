package com.tool.controller;

import com.tool.dto.ApiResponse;
import com.tool.dto.CreateCodeSnippetRequest;
import com.tool.service.CodeSnippetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 代码片段控制器
 * 
 * @author tool-service
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/code-snippets")
@CrossOrigin(origins = "*")
public class CodeSnippetController {
    
    @Autowired
    private CodeSnippetService codeSnippetService;
    
    /**
     * 获取代码片段记录列表
     * 
     * @param page 页码
     * @param size 每页大小
     * @param title 标题过滤
     * @param language 语言过滤
     * @return 代码片段记录列表
     */
    @GetMapping("/records")
    public ApiResponse getRecords(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "language", required = false) String language) {
        return codeSnippetService.getRecords(page, size, title, language);
    }
    
    /**
     * 搜索代码片段记录
     * 
     * @param query 搜索关键词
     * @param page 页码
     * @param size 每页大小
     * @return 搜索结果
     */
    @GetMapping("/records/search")
    public ApiResponse searchRecords(
            @RequestParam("query") String query,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        return codeSnippetService.searchRecords(query, page, size);
    }
    
    /**
     * 简化搜索代码片段记录（兼容前端调用）
     * 
     * @param q 搜索关键词
     * @param page 页码
     * @param size 每页大小
     * @return 搜索结果
     */
    @GetMapping("/search")
    public ApiResponse searchRecordsSimple(
            @RequestParam("q") String q,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        return codeSnippetService.searchRecords(q, page, size);
    }
    
    /**
     * 根据ID获取代码片段记录
     * 
     * @param id 记录ID
     * @return 代码片段记录
     */
    @GetMapping("/records/{id}")
    public ApiResponse getRecordById(@PathVariable("id") Long id) {
        return codeSnippetService.getRecordById(id);
    }
    
    /**
     * 创建代码片段记录
     * 
     * @param request 创建请求
     * @return 创建结果
     */
    @PostMapping("/records")
    public ApiResponse createRecord(@RequestBody CreateCodeSnippetRequest request) {
        return codeSnippetService.createRecord(request);
    }
    
    /**
     * 更新代码片段记录
     * 
     * @param id 记录ID
     * @param request 更新请求
     * @return 更新结果
     */
    @PutMapping("/records/{id}")
    public ApiResponse updateRecord(
            @PathVariable("id") Long id,
            @RequestBody CreateCodeSnippetRequest request) {
        return codeSnippetService.updateRecord(id, request);
    }
    
    /**
     * 删除代码片段记录
     * 
     * @param id 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/records/{id}")
    public ApiResponse deleteRecord(@PathVariable("id") Long id) {
        return codeSnippetService.deleteRecord(id);
    }
    
    /**
     * 获取所有标签名称
     * 
     * @return 标签名称列表
     */
    @GetMapping("/tags")
    public ApiResponse getAllTagNames() {
        return codeSnippetService.getAllTagNames();
    }
    
    /**
     * 清理孤立标签
     * 
     * @return 清理结果
     */
    @DeleteMapping("/tags/orphan")
    public ApiResponse cleanOrphanTags() {
        return codeSnippetService.cleanOrphanTags();
    }
}