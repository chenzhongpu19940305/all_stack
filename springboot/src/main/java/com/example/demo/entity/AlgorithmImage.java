package com.example.demo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AlgorithmImage {
    
    private Long id;
    private Long algorithmId;  // 外键ID
    private String src;
    private String desc;
    private Integer sortOrder = 0;
    private LocalDateTime createdAt;
    
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 