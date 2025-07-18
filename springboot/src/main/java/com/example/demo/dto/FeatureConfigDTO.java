package com.example.demo.dto;

import lombok.Data;

@Data
public class FeatureConfigDTO {
    
    private Long id;
    private Long featureId;
    private String key;
    private String value;
    private String description;
    private String status;
} 