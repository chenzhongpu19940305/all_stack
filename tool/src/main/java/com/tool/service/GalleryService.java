package com.tool.service;

import com.tool.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Gallery服务接口
 * 
 * @author tool-service
 * @version 1.0.0
 */
public interface GalleryService {
    
    /**
     * 获取所有记录
     * 
     * @return 记录列表
     */
    List<GalleryRecordDTO> getAllRecords();
    
    /**
     * 根据ID获取记录
     * 
     * @param id 记录ID
     * @return 记录详情
     */
    GalleryRecordDTO getRecordById(Long id);
    
    /**
     * 搜索记录
     * 
     * @param query 搜索关键词
     * @return 匹配的记录列表
     */
    List<GalleryRecordDTO> searchRecords(String query);
    
    /**
     * 创建新记录
     * 
     * @param request 创建请求
     * @return 创建的记录
     */
    GalleryRecordDTO createRecord(CreateRecordRequest request);
    
    /**
     * 删除记录
     * 
     * @param id 记录ID
     */
    void deleteRecord(Long id);
    
    /**
     * 上传图片
     * 
     * @param file 图片文件
     * @return 上传结果
     */
    GalleryImageDTO uploadImage(MultipartFile file);
} 