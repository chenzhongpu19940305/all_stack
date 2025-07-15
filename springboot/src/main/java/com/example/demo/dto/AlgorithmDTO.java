package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AlgorithmDTO {
    private Long id;
    private String name;
    private String category;
    private String description;
    private String code;
    private List<AlgorithmImageDTO> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 