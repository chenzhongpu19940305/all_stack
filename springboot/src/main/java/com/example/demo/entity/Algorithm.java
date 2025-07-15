package com.example.demo.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Algorithm {
    
    private Long id;
    private String name;
    private String category;
    private String description;
    private String code;
    private List<AlgorithmImage> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 