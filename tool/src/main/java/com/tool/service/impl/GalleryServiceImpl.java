package com.tool.service.impl;

import com.tool.dto.ApiResponse;
import com.tool.dto.GalleryRecordDTO;
import com.tool.dto.GalleryImageDTO;
import com.tool.dto.CreateRecordRequest;
import com.tool.entity.GalleryRecord;
import com.tool.entity.GalleryImage;
import com.tool.mapper.GalleryRecordMapper;
import com.tool.mapper.GalleryImageMapper;
import com.tool.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64;

/**
 * Gallery服务实现类
 * 
 * @author tool-service
 * @version 1.0.0
 */
@Service
public class GalleryServiceImpl implements GalleryService {
    
    @Autowired
    private GalleryRecordMapper galleryRecordMapper;
    
    @Autowired
    private GalleryImageMapper galleryImageMapper;
    
    @Override
    public ApiResponse getRecords(Integer page, Integer size, String sort, String order) {
        try {
            // 设置默认值
            page = page == null ? 1 : page;
            size = size == null ? 20 : size;
            sort = sort == null ? "created_at" : sort;
            order = order == null ? "desc" : order;
            
            // 计算偏移量
            int offset = (page - 1) * size;
            
            // 查询数据
            List<GalleryRecord> records = galleryRecordMapper.selectWithImagesByPage(offset, size);
            int total = galleryRecordMapper.count();
            
            // 转换为DTO
            List<GalleryRecordDTO> recordDTOs = new ArrayList<>();
            for (GalleryRecord record : records) {
                GalleryRecordDTO dto = convertToDTO(record);
                recordDTOs.add(dto);
            }
            
            // 构建响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("records", recordDTOs);
            data.put("total", total);
            data.put("page", page);
            data.put("size", size);
            
            return ApiResponse.success("获取记录成功", data);
        } catch (Exception e) {
            return ApiResponse.error("获取记录失败: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse searchRecords(String query, Integer page, Integer size) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return getRecords(page, size, null, null);
            }
            
            // 设置默认值
            page = page == null ? 1 : page;
            size = size == null ? 20 : size;
            
            // 搜索记录
            List<GalleryRecord> records = galleryRecordMapper.selectByTitle(query.trim());
            int total = galleryRecordMapper.countByTitle(query.trim());
            
            // 转换为DTO
            List<GalleryRecordDTO> recordDTOs = new ArrayList<>();
            for (GalleryRecord record : records) {
                // 查询每个记录的图片
                List<GalleryImage> images = galleryImageMapper.selectByRecordId(record.getId());
                record.setImages(images);
                GalleryRecordDTO dto = convertToDTO(record);
                recordDTOs.add(dto);
            }
            
            // 构建响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("records", recordDTOs);
            data.put("total", total);
            data.put("page", page);
            data.put("size", size);
            
            return ApiResponse.success("搜索记录成功", data);
        } catch (Exception e) {
            return ApiResponse.error("搜索记录失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public ApiResponse createRecord(CreateRecordRequest request) {
        try {
            // 验证参数
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                return ApiResponse.error("标题不能为空");
            }
            if (request.getImages() == null || request.getImages().isEmpty()) {
                return ApiResponse.error("图片不能为空");
            }
            
            // 创建记录
            GalleryRecord record = new GalleryRecord();
            record.setTitle(request.getTitle());
            record.setDescription(request.getDescription());
            record.setCode(request.getCode());
            record.setLanguage(request.getLanguage());
            record.setCreatedAt(LocalDateTime.now());
            record.setUpdatedAt(LocalDateTime.now());
            
            // 插入记录
            galleryRecordMapper.insert(record);
            Long recordId = record.getId();
            
            // 插入图片
            for (GalleryImageDTO imageDTO : request.getImages()) {
                GalleryImage image = new GalleryImage();
                image.setRecordId(recordId);
                image.setName(imageDTO.getName());
                image.setImageData(imageDTO.getImageData());
                image.setFileSize(imageDTO.getFileSize());
                image.setContentType(imageDTO.getContentType());
                image.setCreatedAt(LocalDateTime.now());
                galleryImageMapper.insert(image);
            }
            
            // 查询完整的记录
            GalleryRecord createdRecord = galleryRecordMapper.selectWithImages(recordId);
            GalleryRecordDTO resultDTO = convertToDTO(createdRecord);
            
            return ApiResponse.success("创建记录成功", resultDTO);
        } catch (Exception e) {
            return ApiResponse.error("创建记录失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public ApiResponse updateRecord(Long id, CreateRecordRequest request) {
        try {
            // 验证记录是否存在
            GalleryRecord existingRecord = galleryRecordMapper.selectById(id);
            if (existingRecord == null) {
                return ApiResponse.error("记录不存在");
            }
            
            // 验证参数
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                return ApiResponse.error("标题不能为空");
            }
            if (request.getImages() == null || request.getImages().isEmpty()) {
                return ApiResponse.error("图片不能为空");
            }
            
            // 更新记录
            existingRecord.setTitle(request.getTitle());
            existingRecord.setDescription(request.getDescription());
            existingRecord.setCode(request.getCode());
            existingRecord.setLanguage(request.getLanguage());
            existingRecord.setUpdatedAt(LocalDateTime.now());
            
            galleryRecordMapper.update(existingRecord);
            
            // 删除旧的图片
            galleryImageMapper.deleteByRecordId(id);
            
            // 插入新的图片
            for (GalleryImageDTO imageDTO : request.getImages()) {
                GalleryImage image = new GalleryImage();
                image.setRecordId(id);
                image.setName(imageDTO.getName());
                image.setImageData(imageDTO.getImageData());
                image.setFileSize(imageDTO.getFileSize());
                image.setContentType(imageDTO.getContentType());
                image.setCreatedAt(LocalDateTime.now());
                galleryImageMapper.insert(image);
            }
            
            // 查询更新后的记录
            GalleryRecord updatedRecord = galleryRecordMapper.selectWithImages(id);
            GalleryRecordDTO resultDTO = convertToDTO(updatedRecord);
            
            return ApiResponse.success("更新记录成功", resultDTO);
        } catch (Exception e) {
            return ApiResponse.error("更新记录失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public ApiResponse deleteRecord(Long id) {
        try {
            // 验证记录是否存在
            GalleryRecord existingRecord = galleryRecordMapper.selectById(id);
            if (existingRecord == null) {
                return ApiResponse.error("记录不存在");
            }
            
            // 删除图片
            galleryImageMapper.deleteByRecordId(id);
            
            // 删除记录
            galleryRecordMapper.deleteById(id);
            
            return ApiResponse.success("删除记录成功");
        } catch (Exception e) {
            return ApiResponse.error("删除记录失败: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse getRecord(Long id) {
        try {
            GalleryRecord record = galleryRecordMapper.selectWithImages(id);
            if (record == null) {
                return ApiResponse.error("记录不存在");
            }
            
            // 获取单个记录时包含图片Base64数据
            GalleryRecordDTO resultDTO = convertToDTO(record, true);
            return ApiResponse.success("获取记录成功", resultDTO);
        } catch (Exception e) {
            return ApiResponse.error("获取记录失败: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse uploadImage(MultipartFile file) {
        try {
            // 验证文件
            if (file.isEmpty()) {
                return ApiResponse.error("文件为空");
            }
            
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ApiResponse.error("只支持图片文件");
            }
            
            // 检查文件大小（限制为10MB）
            if (file.getSize() > 10 * 1024 * 1024) {
                return ApiResponse.error("文件大小超过限制（10MB）");
            }
            
            // 转换为Base64
            byte[] imageBytes = file.getBytes();
            String imageDataBase64 = "data:" + contentType + ";base64," + Base64.getEncoder().encodeToString(imageBytes);
            
            // 创建图片DTO
            GalleryImageDTO imageDTO = new GalleryImageDTO();
            imageDTO.setName(file.getOriginalFilename());
            imageDTO.setImageData(imageDataBase64);
            imageDTO.setFileSize(file.getSize());
            imageDTO.setContentType(contentType);
            
            return ApiResponse.success("上传图片成功", imageDTO);
        } catch (Exception e) {
            return ApiResponse.error("上传图片失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public ApiResponse cleanOrphanImages() {
        try {
            // 查询孤立图片
            List<GalleryImage> orphanImages = galleryImageMapper.selectOrphanImages();
            
            if (orphanImages.isEmpty()) {
                return ApiResponse.success("没有孤立图片需要清理");
            }
            
            // 删除孤立图片
            int deletedCount = galleryImageMapper.deleteOrphanImages();
            
            return ApiResponse.success("清理完成，删除了 " + deletedCount + " 张孤立图片");
        } catch (Exception e) {
            return ApiResponse.error("清理孤立图片失败: " + e.getMessage());
        }
    }
    
    /**
     * 将实体转换为DTO（只包含第一张图片的Base64数据，用于列表查询）
     */
    private GalleryRecordDTO convertToDTO(GalleryRecord record) {
        return convertToDTO(record, false, true);
    }
    
    /**
     * 将实体转换为DTO
     * @param record 记录实体
     * @param includeAllImageData 是否包含所有图片的Base64数据
     */
    private GalleryRecordDTO convertToDTO(GalleryRecord record, boolean includeAllImageData) {
        return convertToDTO(record, includeAllImageData, false);
    }
    
    /**
     * 将实体转换为DTO
     * @param record 记录实体
     * @param includeAllImageData 是否包含所有图片的Base64数据
     * @param includeFirstImageOnly 是否只包含第一张图片的Base64数据（用于列表预览）
     */
    private GalleryRecordDTO convertToDTO(GalleryRecord record, boolean includeAllImageData, boolean includeFirstImageOnly) {
        GalleryRecordDTO dto = new GalleryRecordDTO();
        dto.setId(record.getId());
        dto.setTitle(record.getTitle());
        dto.setDescription(record.getDescription());
        dto.setCode(record.getCode());
        dto.setLanguage(record.getLanguage());
        dto.setCreatedAt(record.getCreatedAt());
        dto.setUpdatedAt(record.getUpdatedAt());
        
        // 转换图片
        if (record.getImages() != null) {
            List<GalleryImageDTO> imageDTOs = new ArrayList<>();
            for (int i = 0; i < record.getImages().size(); i++) {
                GalleryImage image = record.getImages().get(i);
                GalleryImageDTO imageDTO = new GalleryImageDTO();
                imageDTO.setId(image.getId());
                imageDTO.setName(image.getName());
                
                // 设置Base64数据的逻辑
                if (includeAllImageData || (includeFirstImageOnly && i == 0)) {
                    imageDTO.setImageData(image.getImageData());
                }
                
                imageDTO.setFileSize(image.getFileSize());
                imageDTO.setContentType(image.getContentType());
                imageDTO.setCreatedAt(image.getCreatedAt());
                imageDTOs.add(imageDTO);
            }
            dto.setImages(imageDTOs);
        }
        
        return dto;
    }
}