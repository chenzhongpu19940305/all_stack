package com.example.demo.controller;

import com.example.demo.dto.GalleryRecordDTO;
import com.example.demo.dto.GalleryImageDTO;
import com.example.demo.entity.GalleryImage;
import com.example.demo.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gallery")
@CrossOrigin(origins = "*")
public class GalleryController {
    
    @Autowired
    private GalleryService galleryService;
    
    /**
     * 获取AI问答记录列表
     */
    @GetMapping("/records")
    public ResponseEntity<Map<String, Object>> getRecords() {
        try {
            List<GalleryRecordDTO> records = galleryService.getAllRecords();
            Map<String, Object> response = new HashMap<>();
            response.put("records", records);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取记录失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取单个记录详情
     */
    @GetMapping("/records/{id}")
    public ResponseEntity<Map<String, Object>> getRecord(@PathVariable Long id) {
        try {
            GalleryRecordDTO record = galleryService.getRecordById(id);
            if (record != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("record", record);
                response.put("success", true);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "记录不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取记录失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 搜索AI问答记录
     */
    @PostMapping("/records/search")
    public ResponseEntity<Map<String, Object>> searchRecords(@RequestBody Map<String, String> request) {
        try {
            String query = request.get("query");
            List<GalleryRecordDTO> records = galleryService.searchRecords(query);
            Map<String, Object> response = new HashMap<>();
            response.put("records", records);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "搜索失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 新增AI问答记录
     */
    @PostMapping("/records")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody GalleryRecordDTO recordDTO) {
        try {
            GalleryRecordDTO createdRecord = galleryService.createRecord(recordDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("record", createdRecord);
            response.put("success", true);
            response.put("message", "记录创建成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "创建记录失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 删除AI问答记录
     */
    @DeleteMapping("/records/{id}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Long id) {
        try {
            galleryService.deleteRecord(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "记录删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除记录失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 上传图片
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            GalleryImageDTO imageDTO = galleryService.uploadImage(file);
            Map<String, Object> response = new HashMap<>();
            response.put("url", imageDTO.getUrl());
            response.put("id", imageDTO.getId());
            response.put("name", imageDTO.getName());
            response.put("success", true);
            response.put("message", "图片上传成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "图片上传失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取图片数据
     */
    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
        try {
            GalleryImage image = galleryService.getImageById(imageId);
            if (image != null && image.getImageData() != null) {
                return ResponseEntity.ok()
                        .header("Content-Type", image.getContentType())
                        .body(image.getImageData());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 