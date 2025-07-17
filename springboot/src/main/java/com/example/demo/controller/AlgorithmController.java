package com.example.demo.controller;

import com.example.demo.dto.AlgorithmDTO;
import com.example.demo.service.AlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/algorithms")
public class AlgorithmController {
    
    @Autowired
    private AlgorithmService algorithmService;
    
    // 获取所有算法
    @GetMapping
    public ResponseEntity<List<AlgorithmDTO>> getAllAlgorithms() {
        List<AlgorithmDTO> algorithms = algorithmService.findAll();
        return ResponseEntity.ok(algorithms);
    }
    
    // 根据ID获取算法
    @GetMapping("/{id}")
    public ResponseEntity<AlgorithmDTO> getAlgorithmById(@PathVariable Long id) {
        try {
            AlgorithmDTO algorithm = algorithmService.findById(id);
            return ResponseEntity.ok(algorithm);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 创建新算法
    @PostMapping
    public ResponseEntity<AlgorithmDTO> createAlgorithm(@RequestBody AlgorithmDTO algorithmDTO) {
        AlgorithmDTO savedAlgorithm = algorithmService.save(algorithmDTO);
        return ResponseEntity.ok(savedAlgorithm);
    }
    
    // 更新算法
    @PutMapping("/{id}")
    public ResponseEntity<AlgorithmDTO> updateAlgorithm(@PathVariable Long id, @RequestBody AlgorithmDTO algorithmDTO) {
        try {
            AlgorithmDTO updatedAlgorithm = algorithmService.update(id, algorithmDTO);
            return ResponseEntity.ok(updatedAlgorithm);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 删除算法
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlgorithm(@PathVariable Long id) {
        try {
            algorithmService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 根据分类获取算法
    @GetMapping("/category/{category}")
    public ResponseEntity<List<AlgorithmDTO>> getAlgorithmsByCategory(@PathVariable String category) {
        List<AlgorithmDTO> algorithms = algorithmService.findByCategory(category);
        return ResponseEntity.ok(algorithms);
    }
    
    // 获取所有分类
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = algorithmService.findAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    // 根据名称搜索算法
    @GetMapping("/search")
    public ResponseEntity<List<AlgorithmDTO>> searchAlgorithms(@RequestParam String name) {
        List<AlgorithmDTO> algorithms = algorithmService.findByNameContaining(name);
        return ResponseEntity.ok(algorithms);
    }
    
    // 新增：获取算法列表（POST请求）
    @PostMapping("/list")
    public ResponseEntity<List<AlgorithmDTO>> getAlgorithmsList(@RequestBody(required = false) Object request) {
        List<AlgorithmDTO> algorithms = algorithmService.findAll();
        return ResponseEntity.ok(algorithms);
    }
    
    // 新增：创建算法（POST请求）
    @PostMapping("/create")
    public ResponseEntity<AlgorithmDTO> createAlgorithmPost(@RequestBody AlgorithmDTO algorithmDTO) {
        AlgorithmDTO savedAlgorithm = algorithmService.save(algorithmDTO);
        return ResponseEntity.ok(savedAlgorithm);
    }
    
    // 新增：更新算法（POST请求）
    @PostMapping("/update")
    public ResponseEntity<AlgorithmDTO> updateAlgorithmPost(@RequestBody AlgorithmDTO algorithmDTO) {
        try {
            AlgorithmDTO updatedAlgorithm = algorithmService.update(algorithmDTO.getId(), algorithmDTO);
            return ResponseEntity.ok(updatedAlgorithm);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 新增：删除算法（POST请求）
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteAlgorithmPost(@RequestBody DeleteRequest request) {
        try {
            algorithmService.deleteById(request.getId());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 删除请求的内部类
    public static class DeleteRequest {
        private Long id;
        
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
    }
} 