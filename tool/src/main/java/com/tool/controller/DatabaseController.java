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
     * 初始化数据库表
     * 
     * @return 初始化结果
     */
    @PostMapping("/init")
    public ResponseEntity<Map<String, Object>> initDatabase() {
        Map<String, Object> response = new HashMap<String, Object>();
        
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            
            // 创建gallery_records表
            String createRecordsTable = 
                "CREATE TABLE IF NOT EXISTS gallery_records (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(255) NOT NULL COMMENT '记录标题'," +
                "created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "INDEX idx_title (title)," +
                "INDEX idx_created_at (created_at)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI问答记录表'";
            
            statement.executeUpdate(createRecordsTable);
            
            // 创建gallery_images表
            String createImagesTable = 
                "CREATE TABLE IF NOT EXISTS gallery_images (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL COMMENT '图片名称'," +
                "image_data LONGTEXT NOT NULL COMMENT '图片base64数据'," +
                "file_size BIGINT COMMENT '文件大小(字节)'," +
                "content_type VARCHAR(100) COMMENT '文件类型'," +
                "created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "record_id BIGINT NOT NULL COMMENT '关联的记录ID'," +
                "FOREIGN KEY (record_id) REFERENCES gallery_records(id) ON DELETE CASCADE," +
                "INDEX idx_record_id (record_id)," +
                "INDEX idx_created_at (created_at)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图片表'";
            
            statement.executeUpdate(createImagesTable);
            
            // 插入示例数据
            String insertSampleData = 
                "INSERT INTO gallery_records (title, created_at) VALUES " +
                "('Java算法实现示例', NOW())," +
                "('Spring Boot项目结构', NOW())," +
                "('Vue.js前端开发', NOW()) " +
                "ON DUPLICATE KEY UPDATE title = title";
            
            statement.executeUpdate(insertSampleData);
            
            response.put("message", "数据库初始化成功");
            response.put("status", "success");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("error", "数据库初始化失败: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
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