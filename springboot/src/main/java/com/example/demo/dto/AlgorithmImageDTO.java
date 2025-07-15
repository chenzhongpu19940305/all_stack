package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AlgorithmImageDTO {
    private Long id;
    private String src;
    private String desc;
    private Integer sortOrder;
    private LocalDateTime createdAt;
} 