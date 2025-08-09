package com.tool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库初始化控制器
 * 
 * @author tool-service
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/database")
@CrossOrigin(origins = "*")
public class DatabaseController {
    
    @Autowired
    private DataSource dataSource;
    

    
    /**
     * 检查数据库连接
     * 
     * @return 连接状态
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> checkDatabaseHealth() {
        Map<String, Object> response = new HashMap<String, Object>();
        
        try (Connection connection = dataSource.getConnection()) {
            response.put("status", "healthy");
            response.put("message", "数据库连接正常");
            response.put("database", connection.getMetaData().getDatabaseProductName());
            response.put("version", connection.getMetaData().getDatabaseProductVersion());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "unhealthy");
            response.put("error", "数据库连接失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}