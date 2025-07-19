package com.tool.controller;

import com.tool.dto.ApiResponse;
import com.tool.dto.VideoRecordDTO;
import com.tool.dto.SearchVideoRequest;
import com.tool.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 视频控制器
 */
@RestController
@RequestMapping("/api/videogallery")
@CrossOrigin(origins = "*")
public class VideoController {
    
    @Autowired
    private VideoService videoService;
    
    /**
     * 获取视频记录列表
     */
    @GetMapping("/records")
    public ApiResponse getVideoRecords(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order) {
        return videoService.getVideoRecords(page, size, sort, order);
    }
    
    /**
     * 搜索视频记录
     */
    @PostMapping("/records/search")
    public ApiResponse searchVideoRecords(@RequestBody SearchVideoRequest request) {
        return videoService.searchVideoRecords(request);
    }
    
    /**
     * 创建视频记录
     */
    @PostMapping("/records")
    public ApiResponse createVideoRecord(@RequestBody VideoRecordDTO recordDTO) {
        return videoService.createVideoRecord(recordDTO);
    }
    
    /**
     * 更新视频记录
     */
    @PutMapping("/records/{id}")
    public ApiResponse updateVideoRecord(@PathVariable Long id, @RequestBody VideoRecordDTO recordDTO) {
        return videoService.updateVideoRecord(id, recordDTO);
    }
    
    /**
     * 删除视频记录
     */
    @DeleteMapping("/records/{id}")
    public ApiResponse deleteVideoRecord(@PathVariable Long id) {
        return videoService.deleteVideoRecord(id);
    }
    
    /**
     * 获取单个视频记录
     */
    @GetMapping("/records/{id}")
    public ApiResponse getVideoRecord(@PathVariable Long id) {
        return videoService.getVideoRecord(id);
    }
    
    /**
     * 上传视频文件
     * 注意：此接口预留，您需要自己实现视频保存逻辑
     */
    @PostMapping("/upload")
    public ApiResponse uploadVideo(@RequestParam("video") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ApiResponse.error("上传文件不能为空");
            }
            
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("video/")) {
                return ApiResponse.error("只支持视频文件上传");
            }
            
            // 检查文件大小（100MB限制）
            if (file.getSize() > 100 * 1024 * 1024) {
                return ApiResponse.error("文件大小不能超过100MB");
            }
            
            // 获取文件数据
            byte[] videoData = file.getBytes();
            String fileName = file.getOriginalFilename();
            
            // 调用服务层上传视频
            return videoService.uploadVideo(videoData, fileName, contentType);
            
        } catch (IOException e) {
            return ApiResponse.error("文件读取失败: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("上传失败: " + e.getMessage());
        }
    }
} 