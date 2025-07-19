package com.tool.service;

import com.tool.dto.VideoRecordDTO;
import com.tool.dto.SearchVideoRequest;
import com.tool.dto.ApiResponse;

import java.util.List;

/**
 * 视频服务接口
 */
public interface VideoService {
    
    /**
     * 获取视频记录列表
     */
    ApiResponse getVideoRecords(Integer page, Integer size, String sort, String order);
    
    /**
     * 搜索视频记录
     */
    ApiResponse searchVideoRecords(SearchVideoRequest request);
    
    /**
     * 创建视频记录
     */
    ApiResponse createVideoRecord(VideoRecordDTO recordDTO);
    
    /**
     * 更新视频记录
     */
    ApiResponse updateVideoRecord(Long id, VideoRecordDTO recordDTO);
    
    /**
     * 删除视频记录
     */
    ApiResponse deleteVideoRecord(Long id);
    
    /**
     * 获取单个视频记录
     */
    ApiResponse getVideoRecord(Long id);
    
    /**
     * 上传视频文件
     * 注意：此方法预留，您需要自己实现视频保存逻辑
     */
    ApiResponse uploadVideo(byte[] videoData, String fileName, String contentType);
} 