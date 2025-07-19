package com.tool.controller;

import com.tool.mapper.GalleryRecordMapper;
import com.tool.entity.GalleryRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试控制器
 * 
 * @author tool-service
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {
    
    @Autowired
    private GalleryRecordMapper recordMapper;
    
    /**
     * 测试MyBatis映射
     * 
     * @return 测试结果
     */
    @GetMapping("/mybatis")
    public ResponseEntity<Map<String, Object>> testMyBatis() {
        Map<String, Object> response = new HashMap<String, Object>();
        
        try {
            // 测试查询所有记录
            List<GalleryRecord> records = recordMapper.selectAll();
            
            response.put("success", true);
            response.put("message", "MyBatis映射正常");
            response.put("recordCount", records.size());
            response.put("records", records);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "MyBatis映射失败: " + e.getMessage());
            response.put("exception", e.getClass().getName());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 测试数据库连接
     * 
     * @return 连接状态
     */
    @GetMapping("/connection")
    public ResponseEntity<Map<String, Object>> testConnection() {
        Map<String, Object> response = new HashMap<String, Object>();
        
        try {
            // 执行一个简单的查询来测试连接
            List<GalleryRecord> records = recordMapper.selectAll();
            
            response.put("success", true);
            response.put("message", "数据库连接正常");
            response.put("recordCount", records.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "数据库连接失败: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
} 