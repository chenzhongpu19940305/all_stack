package com.tool.service.impl;

import com.tool.dto.*;
import com.tool.entity.GalleryImage;
import com.tool.entity.GalleryRecord;
import com.tool.mapper.GalleryImageMapper;
import com.tool.mapper.GalleryRecordMapper;
import com.tool.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Base64;
import java.util.stream.Collectors;

/**
 * Gallery服务实现类
 * 
 * @author tool-service
 * @version 1.0.0
 */
@Service
@Transactional
public class GalleryServiceImpl implements GalleryService {
    
    @Autowired
    private GalleryRecordMapper recordMapper;
    
    @Autowired
    private GalleryImageMapper imageMapper;
    
    @Override
    public List<GalleryRecordDTO> getAllRecords() {
        List<GalleryRecord> records = recordMapper.selectAll();
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public GalleryRecordDTO getRecordById(Long id) {
        GalleryRecord record = recordMapper.selectById(id);
        if (record == null) {
            throw new RuntimeException("记录不存在: " + id);
        }
        return convertToDTO(record);
    }
    
    @Override
    public List<GalleryRecordDTO> searchRecords(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllRecords();
        }
        
        List<GalleryRecord> records = recordMapper.selectByTitle(query.trim());
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public GalleryRecordDTO createRecord(CreateRecordRequest request) {
        // 创建记录
        GalleryRecord record = new GalleryRecord(request.getTitle(), request.getDescription());
        recordMapper.insert(record);
        
        // 如果有图片，创建图片记录
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (GalleryImageDTO imageDTO : request.getImages()) {
                GalleryImage image = new GalleryImage();
                image.setName(imageDTO.getName());
                image.setImageData(imageDTO.getImageData());
                image.setFileSize(imageDTO.getFileSize());
                image.setContentType(imageDTO.getContentType());
                image.setRecordId(record.getId());
                imageMapper.insert(image);
            }
        }
        
        // 重新查询以获取完整的记录信息（包括图片）
        return getRecordById(record.getId());
    }
    
    @Override
    public void deleteRecord(Long id) {
        GalleryRecord record = recordMapper.selectById(id);
        if (record == null) {
            throw new RuntimeException("记录不存在: " + id);
        }
        
        // 删除图片记录（数据库中直接删除，无需删除文件）
        imageMapper.deleteByRecordId(id);
        
        // 删除记录
        recordMapper.deleteById(id);
    }
    
    @Override
    public GalleryImageDTO uploadImage(MultipartFile file) {
        try {
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("只能上传图片文件");
            }
            
            // 将图片转换为base64
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            
            // 创建图片记录
            GalleryImage image = new GalleryImage();
            image.setName(file.getOriginalFilename() != null ? file.getOriginalFilename() : "image");
            image.setImageData("data:" + contentType + ";base64," + base64Image);
            image.setFileSize(file.getSize());
            image.setContentType(contentType);
            
            imageMapper.insert(image);
            
            return convertImageToDTO(image);
            
        } catch (IOException e) {
            throw new RuntimeException("图片上传失败", e);
        }
    }
    
    private GalleryRecordDTO convertToDTO(GalleryRecord record) {
        GalleryRecordDTO dto = new GalleryRecordDTO(
                record.getId(),
                record.getTitle(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
        
        // 设置描述
        dto.setDescription(record.getDescription());
        
        // 设置关联的图片（如果record中已经包含图片信息）
        if (record.getImages() != null && !record.getImages().isEmpty()) {
            List<GalleryImageDTO> imageDTOs = record.getImages().stream()
                    .map(this::convertImageToDTO)
                    .collect(Collectors.toList());
            dto.setImages(imageDTOs);
        } else {
            // 如果record中没有图片信息，则单独查询
            List<GalleryImage> images = imageMapper.selectByRecordId(record.getId());
            if (images != null && !images.isEmpty()) {
                List<GalleryImageDTO> imageDTOs = images.stream()
                        .map(this::convertImageToDTO)
                        .collect(Collectors.toList());
                dto.setImages(imageDTOs);
            }
        }
        
        return dto;
    }
    
    private GalleryImageDTO convertImageToDTO(GalleryImage image) {
        GalleryImageDTO dto = new GalleryImageDTO(
                image.getId(),
                image.getName(),
                image.getImageData()
        );
        dto.setFileSize(image.getFileSize());
        dto.setContentType(image.getContentType());
        dto.setCreatedAt(image.getCreatedAt());
        return dto;
    }
} 