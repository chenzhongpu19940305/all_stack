package com.example.demo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    
    private Long id;
    private String username;
    private String password;
    private String email;
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