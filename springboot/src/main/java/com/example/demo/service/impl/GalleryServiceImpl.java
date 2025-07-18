package com.example.demo.service.impl;

import com.example.demo.dto.GalleryImageDTO;
import com.example.demo.dto.GalleryRecordDTO;
import com.example.demo.entity.GalleryImage;
import com.example.demo.entity.GalleryRecord;
import com.example.demo.mapper.GalleryImageMapper;
import com.example.demo.mapper.GalleryRecordMapper;
import com.example.demo.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GalleryServiceImpl implements GalleryService {
    
    @Autowired
    private GalleryRecordMapper recordMapper;
    
    @Autowired
    private GalleryImageMapper imageMapper;
    
    @Override
    public List<GalleryRecordDTO> getAllRecords() {
        List<GalleryRecord> records = recordMapper.findAll();
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public GalleryRecordDTO getRecordById(Long id) {
        GalleryRecord record = recordMapper.findById(id);
        if (record != null) {
            return convertToDTO(record);
        }
        return null;
    }
    
    @Override
    public List<GalleryRecordDTO> searchRecords(String query) {
        List<GalleryRecord> records = recordMapper.searchByTitle(query);
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public GalleryRecordDTO createRecord(GalleryRecordDTO recordDTO) {
        GalleryRecord record = new GalleryRecord();
        record.setTitle(recordDTO.getTitle());
        
        recordMapper.insert(record);
        
        if (recordDTO.getImages() != null) {
            for (GalleryImageDTO imageDTO : recordDTO.getImages()) {
                // 更新已上传图片的record_id
                if (imageDTO.getId() != null) {
                    imageMapper.updateRecordId(imageDTO.getId(), record.getId());
                }
            }
        }
        
        return convertToDTO(record);
    }
    
    @Override
    @Transactional
    public void deleteRecord(Long id) {
        imageMapper.deleteByRecordId(id);
        recordMapper.deleteById(id);
    }
    
    @Override
    public GalleryImageDTO uploadImage(MultipartFile file) {
        try {
            // 保存图片到数据库
            GalleryImage image = new GalleryImage();
            image.setName(file.getOriginalFilename());
            image.setContentType(file.getContentType());
            image.setImageData(file.getBytes());
            // record_id 暂时为null，在创建记录时再关联
            
            imageMapper.insert(image);
            
            // 返回图片信息
            GalleryImageDTO dto = new GalleryImageDTO();
            dto.setId(image.getId());
            dto.setName(file.getOriginalFilename());
            dto.setUrl("/api/gallery/images/" + image.getId()); // 通过API获取图片
            
            return dto;
            
        } catch (IOException e) {
            throw new RuntimeException("图片上传失败", e);
        }
    }
    
    @Override
    public GalleryImage getImageById(Long imageId) {
        return imageMapper.findById(imageId);
    }
    
    private GalleryRecordDTO convertToDTO(GalleryRecord record) {
        GalleryRecordDTO dto = new GalleryRecordDTO();
        dto.setId(record.getId());
        dto.setTitle(record.getTitle());
        
        // 获取关联的图片
        List<GalleryImage> images = imageMapper.findByRecordId(record.getId());
        List<GalleryImageDTO> imageDTOs = images.stream()
                .map(this::convertImageToDTO)
                .collect(Collectors.toList());
        dto.setImages(imageDTOs);
        
        return dto;
    }
    
    private GalleryImageDTO convertImageToDTO(GalleryImage image) {
        GalleryImageDTO dto = new GalleryImageDTO();
        dto.setId(image.getId());
        dto.setName(image.getName());
        dto.setUrl("/api/gallery/images/" + image.getId()); // 通过API获取图片
        return dto;
    }
} 