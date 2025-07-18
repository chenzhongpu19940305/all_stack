package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class FeatureDTO {
    
    private Long id;
    private String name;
    private String code;
    private String description;
    private String applicationCode;
    private String deploymentUnitCode;
    private String imageUrl;
    private String status;
    private List<FeatureConfigDTO> configs;
} 