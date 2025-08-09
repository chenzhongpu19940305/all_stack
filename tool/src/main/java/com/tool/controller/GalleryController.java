package com.tool.controller;

import com.tool.dto.ApiResponse;
import com.tool.dto.CreateRecordRequest;
import com.tool.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Gallery控制器
 * 
 * @author tool-service
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/gallery")
@CrossOrigin(origins = "*")
public class GalleryController {
    
    @Autowired
    private GalleryService galleryService;
    
    /**
     * 获取记录列表
     */
    @GetMapping("/records")
    public ResponseEntity<ApiResponse> getRecords(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "sort", defaultValue = "created_at") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order) {
        
        ApiResponse response = galleryService.getRecords(page, size, sort, order);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 搜索记录
     */
    @PostMapping("/records/search")
    public ResponseEntity<ApiResponse> searchRecords(@RequestBody Map<String, Object> request) {
        String query = (String) request.get("query");
        Integer page = (Integer) request.get("page");
        Integer size = (Integer) request.get("size");
        
        ApiResponse response = galleryService.searchRecords(query, page, size);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 创建记录
     */
    @PostMapping("/records")
    public ResponseEntity<ApiResponse> createRecord(@RequestBody CreateRecordRequest request) {
        ApiResponse response = galleryService.createRecord(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 更新记录
     */
    @PutMapping("/records/{id}")
    public ResponseEntity<ApiResponse> updateRecord(
            @PathVariable Long id,
            @RequestBody CreateRecordRequest request) {
        
        ApiResponse response = galleryService.updateRecord(id, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 删除记录
     */
    @DeleteMapping("/records/{id}")
    public ResponseEntity<ApiResponse> deleteRecord(@PathVariable Long id) {
        ApiResponse response = galleryService.deleteRecord(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取单个记录
     */
    @GetMapping("/records/{id}")
    public ResponseEntity<ApiResponse> getRecord(@PathVariable Long id) {
        ApiResponse response = galleryService.getRecord(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 上传图片
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadImage(@RequestParam("image") MultipartFile file) {
        ApiResponse response = galleryService.uploadImage(file);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 清理孤立图片
     */
    @PostMapping("/clean-orphan-images")
    public ResponseEntity<ApiResponse> cleanOrphanImages() {
        ApiResponse response = galleryService.cleanOrphanImages();
        return ResponseEntity.ok(response);
    }
}