package com.tool.controller;

import com.tool.dto.ContentDTO;
import com.tool.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 内容控制器
 */
@RestController
@RequestMapping("/api/content")
@CrossOrigin(origins = "*")
public class ContentController {

    @Autowired
    private ContentService contentService;

    /**
     * 搜索内容
     */
    @GetMapping("/search")
    public ResponseEntity<List<ContentDTO>> searchContent(@RequestParam String q) {
        List<ContentDTO> results = contentService.searchContent(q);
        return ResponseEntity.ok(results);
    }

    /**
     * 根据ID获取内容
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContentDTO> getContentById(@PathVariable Long id) {
        ContentDTO content = contentService.getContentById(id);
        if (content != null) {
            return ResponseEntity.ok(content);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 创建新内容
     */
    @PostMapping
    public ResponseEntity<ContentDTO> createContent(@RequestBody ContentDTO contentDTO) {
        ContentDTO createdContent = contentService.createContent(contentDTO);
        return ResponseEntity.ok(createdContent);
    }

    /**
     * 更新内容
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContentDTO> updateContent(@PathVariable Long id, @RequestBody ContentDTO contentDTO) {
        ContentDTO updatedContent = contentService.updateContent(id, contentDTO);
        if (updatedContent != null) {
            return ResponseEntity.ok(updatedContent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 删除内容
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteContent(@PathVariable Long id) {
        boolean deleted = contentService.deleteContent(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "内容删除成功"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取所有内容
     */
    @GetMapping
    public ResponseEntity<List<ContentDTO>> getAllContent() {
        List<ContentDTO> allContent = contentService.getAllContent();
        return ResponseEntity.ok(allContent);
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "OK", "message", "企业知识管理系统运行正常"));
    }
} 