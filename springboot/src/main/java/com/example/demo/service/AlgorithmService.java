package com.example.demo.service;

import com.example.demo.dto.AlgorithmDTO;
import java.util.List;

public interface AlgorithmService {
    
    List<AlgorithmDTO> findAll();
    
    AlgorithmDTO findById(Long id);
    
    AlgorithmDTO save(AlgorithmDTO algorithmDTO);
    
    AlgorithmDTO update(Long id, AlgorithmDTO algorithmDTO);
    
    void deleteById(Long id);
    
    List<AlgorithmDTO> findByCategory(String category);
    
    List<String> findAllCategories();
    
    List<AlgorithmDTO> findByNameContaining(String name);
} 