package com.tool.service;

import com.tool.dto.ApiResponse;
import com.tool.dto.GalleryRecordDTO;
import com.tool.dto.CreateRecordRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * Gallery服务接口
 * 
 * @author tool-service
 * @version 1.0.0
 */
public interface GalleryService {
    
    /**
     * 获取记录列表
     */
    ApiResponse getRecords(Integer page, Integer size, String sort, String order);
    
    /**
     * 搜索记录
     */
    ApiResponse searchRecords(String query, Integer page, Integer size);
    
    /**
     * 创建记录
     */
    ApiResponse createRecord(CreateRecordRequest request);
    
    /**
     * 更新记录
     */
    ApiResponse updateRecord(Long id, CreateRecordRequest request);
    
    /**
     * 删除记录
     */
    ApiResponse deleteRecord(Long id);
    
    /**
     * 获取单个记录
     */
    ApiResponse getRecord(Long id);
    
    /**
     * 上传图片
     */
    ApiResponse uploadImage(MultipartFile file);
    
    /**
     * 清理孤立图片
     */
    ApiResponse cleanOrphanImages();
}