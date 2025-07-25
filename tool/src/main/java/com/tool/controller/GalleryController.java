package com.tool.controller;

import com.tool.dto.*;
import com.tool.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
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
     * 获取AI问答记录列表
     * 
     * @return 记录列表
     */
    @GetMapping("/records")
    public ResponseEntity<Map<String, Object>> getRecords() {
        try {
            List<GalleryRecordDTO> records = galleryService.getAllRecords();
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("records", records);
            response.put("total", records.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<String, Object>();
            error.put("error", "获取记录失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * 搜索AI问答记录
     * 
     * @param request 搜索请求
     * @return 匹配的记录列表
     */
    @PostMapping("/records/search")
    public ResponseEntity<Map<String, Object>> searchRecords(@RequestBody SearchRequest request) {
        try {
            List<GalleryRecordDTO> records = galleryService.searchRecords(request.getQuery());
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("records", records);
            response.put("total", records.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<String, Object>();
            error.put("error", "搜索记录失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * 新增AI问答记录
     * 
     * @param request 创建请求
     * @return 创建的记录
     */
    @PostMapping("/records")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody CreateRecordRequest request) {
        try {
            GalleryRecordDTO record = galleryService.createRecord(request);
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("record", record);
            response.put("message", "记录创建成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<String, Object>();
            error.put("error", "创建记录失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * 删除AI问答记录
     * 
     * @param id 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/records/{id}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Long id) {
        try {
            galleryService.deleteRecord(id);
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("message", "记录删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<String, Object>();
            error.put("error", "删除记录失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * 更新AI问答记录
     * 
     * @param id 记录ID
     * @param request 更新请求
     * @return 更新后的记录
     */
    @PutMapping("/records/{id}")
    public ResponseEntity<Map<String, Object>> updateRecord(@PathVariable Long id, @RequestBody CreateRecordRequest request) {
        try {
            GalleryRecordDTO record = galleryService.updateRecord(id, request);
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("record", record);
            response.put("message", "记录更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<String, Object>();
            error.put("error", "更新记录失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * 获取单个记录详情
     * 
     * @param id 记录ID
     * @return 记录详情
     */
    @GetMapping("/records/{id}")
    public ResponseEntity<Map<String, Object>> getRecord(@PathVariable Long id) {
        try {
            GalleryRecordDTO record = galleryService.getRecordById(id);
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("record", record);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<String, Object>();
            error.put("error", "获取记录详情失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * 上传图片
     * 
     * @param image 图片文件
     * @return 上传结果
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            if (image.isEmpty()) {
                Map<String, Object> error = new HashMap<String, Object>();
                error.put("error", "请选择要上传的图片");
                return ResponseEntity.badRequest().body(error);
            }
            
            // 检查文件类型
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                Map<String, Object> error = new HashMap<String, Object>();
                error.put("error", "只能上传图片文件");
                return ResponseEntity.badRequest().body(error);
            }
            
            GalleryImageDTO uploadedImage = galleryService.uploadImage(image);
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("id", uploadedImage.getId());
            response.put("name", uploadedImage.getName());
            response.put("imageData", uploadedImage.getImageData());
            response.put("contentType", uploadedImage.getContentType());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<String, Object>();
            error.put("error", "图片上传失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
} 