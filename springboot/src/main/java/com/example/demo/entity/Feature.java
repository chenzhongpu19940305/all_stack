package com.example.demo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Feature {
    
    private Long id;
    private String name;
    private String code;
    private String description;
    private String applicationCode;
    private String deploymentUnitCode;
    private String imageUrl;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = "enabled";
    }
    
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 