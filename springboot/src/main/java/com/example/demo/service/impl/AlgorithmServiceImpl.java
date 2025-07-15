package com.example.demo.service.impl;

import com.example.demo.dto.AlgorithmDTO;
import com.example.demo.dto.AlgorithmImageDTO;
import com.example.demo.entity.Algorithm;
import com.example.demo.entity.AlgorithmImage;
import com.example.demo.repository.AlgorithmRepository;
import com.example.demo.repository.AlgorithmImageRepository;
import com.example.demo.service.AlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlgorithmServiceImpl implements AlgorithmService {
    
    @Autowired
    private AlgorithmRepository algorithmRepository;
    
    @Autowired
    private AlgorithmImageRepository algorithmImageRepository;
    
    @Override
    public List<AlgorithmDTO> findAll() {
        return algorithmRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public AlgorithmDTO findById(Long id) {
        Algorithm algorithm = algorithmRepository.findById(id);
        if (algorithm == null) {
            throw new RuntimeException("Algorithm not found with id: " + id);
        }
        return convertToDTO(algorithm);
    }
    
    @Override
    public AlgorithmDTO save(AlgorithmDTO algorithmDTO) {
        Algorithm algorithm = convertToEntity(algorithmDTO);
        algorithm.onCreate();
        algorithmRepository.insert(algorithm);
        
        // 保存图片数据
        if (algorithmDTO.getImages() != null) {
            for (int i = 0; i < algorithmDTO.getImages().size(); i++) {
                AlgorithmImageDTO imageDTO = algorithmDTO.getImages().get(i);
                AlgorithmImage image = new AlgorithmImage();
                image.setAlgorithmId(algorithm.getId());
                image.setSrc(imageDTO.getSrc());
                image.setDesc(imageDTO.getDesc());
                image.setSortOrder(i);
                image.onCreate();
                algorithmImageRepository.insert(image);
            }
        }
        
        return convertToDTO(algorithm);
    }
    
    @Override
    public AlgorithmDTO update(Long id, AlgorithmDTO algorithmDTO) {
        Algorithm existingAlgorithm = algorithmRepository.findById(id);
        if (existingAlgorithm == null) {
            throw new RuntimeException("Algorithm not found with id: " + id);
        }
        
        // 更新基本信息
        existingAlgorithm.setName(algorithmDTO.getName());
        existingAlgorithm.setCategory(algorithmDTO.getCategory());
        existingAlgorithm.setDescription(algorithmDTO.getDescription());
        existingAlgorithm.setCode(algorithmDTO.getCode());
        existingAlgorithm.onUpdate();
        
        algorithmRepository.update(existingAlgorithm);
        
        // 处理图片数据
        if (algorithmDTO.getImages() != null) {
            // 先删除旧的图片
            algorithmImageRepository.deleteByAlgorithmId(id);
            
            // 保存新的图片
            for (int i = 0; i < algorithmDTO.getImages().size(); i++) {
                AlgorithmImageDTO imageDTO = algorithmDTO.getImages().get(i);
                AlgorithmImage image = new AlgorithmImage();
                image.setAlgorithmId(id);
                image.setSrc(imageDTO.getSrc());
                image.setDesc(imageDTO.getDesc());
                image.setSortOrder(i);
                image.onCreate();
                algorithmImageRepository.insert(image);
            }
        }
        
        return convertToDTO(existingAlgorithm);
    }
    
    @Override
    public void deleteById(Long id) {
        // 先删除图片
        algorithmImageRepository.deleteByAlgorithmId(id);
        // 再删除算法
        algorithmRepository.deleteById(id);
    }
    
    @Override
    public List<AlgorithmDTO> findByCategory(String category) {
        return algorithmRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> findAllCategories() {
        return algorithmRepository.findAllCategories();
    }
    
    @Override
    public List<AlgorithmDTO> findByNameContaining(String name) {
        return algorithmRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private AlgorithmDTO convertToDTO(Algorithm algorithm) {
        AlgorithmDTO dto = new AlgorithmDTO();
        dto.setId(algorithm.getId());
        dto.setName(algorithm.getName());
        dto.setCategory(algorithm.getCategory());
        dto.setDescription(algorithm.getDescription());
        dto.setCode(algorithm.getCode());
        dto.setCreatedAt(algorithm.getCreatedAt());
        dto.setUpdatedAt(algorithm.getUpdatedAt());
        
        // 加载图片数据
        List<AlgorithmImage> images = algorithmImageRepository.findByAlgorithmId(algorithm.getId());
        if (images != null && !images.isEmpty()) {
            dto.setImages(images.stream()
                    .map(this::convertImageToDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    private AlgorithmImageDTO convertImageToDTO(AlgorithmImage image) {
        AlgorithmImageDTO dto = new AlgorithmImageDTO();
        dto.setId(image.getId());
        dto.setSrc(image.getSrc());
        dto.setDesc(image.getDesc());
        dto.setSortOrder(image.getSortOrder());
        dto.setCreatedAt(image.getCreatedAt());
        return dto;
    }
    
    private Algorithm convertToEntity(AlgorithmDTO dto) {
        Algorithm algorithm = new Algorithm();
        algorithm.setId(dto.getId());
        algorithm.setName(dto.getName());
        algorithm.setCategory(dto.getCategory());
        algorithm.setDescription(dto.getDescription());
        algorithm.setCode(dto.getCode());
        
        if (dto.getImages() != null) {
            for (int i = 0; i < dto.getImages().size(); i++) {
                AlgorithmImageDTO imageDTO = dto.getImages().get(i);
                AlgorithmImage image = new AlgorithmImage();
                image.setAlgorithmId(algorithm.getId());
                image.setSrc(imageDTO.getSrc());
                image.setDesc(imageDTO.getDesc());
                image.setSortOrder(i);
                algorithm.getImages().add(image);
            }
        }
        
        return algorithm;
    }
} 