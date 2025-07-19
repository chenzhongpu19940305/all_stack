package com.tool.service.impl;

import com.tool.dto.ApiResponse;
import com.tool.dto.VideoRecordDTO;
import com.tool.dto.VideoFileDTO;
import com.tool.dto.SearchVideoRequest;
import com.tool.entity.VideoRecord;
import com.tool.entity.VideoFile;
import com.tool.mapper.VideoRecordMapper;
import com.tool.mapper.VideoFileMapper;
import com.tool.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视频服务实现类
 */
@Service
public class VideoServiceImpl implements VideoService {
    
    @Autowired
    private VideoRecordMapper videoRecordMapper;
    
    @Autowired
    private VideoFileMapper videoFileMapper;
    
    @Override
    public ApiResponse getVideoRecords(Integer page, Integer size, String sort, String order) {
        try {
            // 设置默认值
            page = page == null ? 1 : page;
            size = size == null ? 20 : size;
            sort = sort == null ? "createTime" : sort;
            order = order == null ? "desc" : order;
            
            // 计算偏移量
            int offset = (page - 1) * size;
            
            // 查询数据
            List<VideoRecord> records = videoRecordMapper.selectByPage(offset, size);
            int total = videoRecordMapper.count();
            
            // 转换为DTO
            List<VideoRecordDTO> recordDTOs = new ArrayList<>();
            for (VideoRecord record : records) {
                VideoRecordDTO dto = convertToDTO(record);
                recordDTOs.add(dto);
            }
            
            // 构建响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("records", recordDTOs);
            data.put("total", total);
            data.put("page", page);
            data.put("size", size);
            
            return ApiResponse.success("获取视频记录成功", data);
        } catch (Exception e) {
            return ApiResponse.error("获取视频记录失败: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse searchVideoRecords(SearchVideoRequest request) {
        try {
            if (request.getQuery() == null || request.getQuery().trim().isEmpty()) {
                return getVideoRecords(request.getPage(), request.getSize(), null, null);
            }
            
            // 设置默认值
            Integer page = request.getPage() == null ? 1 : request.getPage();
            Integer size = request.getSize() == null ? 20 : request.getSize();
            
            // 搜索记录
            List<VideoRecord> records = videoRecordMapper.selectByTitle(request.getQuery());
            int total = videoRecordMapper.countByTitle(request.getQuery());
            
            // 转换为DTO
            List<VideoRecordDTO> recordDTOs = new ArrayList<>();
            for (VideoRecord record : records) {
                VideoRecordDTO dto = convertToDTO(record);
                recordDTOs.add(dto);
            }
            
            // 构建响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("records", recordDTOs);
            data.put("total", total);
            data.put("page", page);
            data.put("size", size);
            
            return ApiResponse.success("搜索视频记录成功", data);
        } catch (Exception e) {
            return ApiResponse.error("搜索视频记录失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public ApiResponse createVideoRecord(VideoRecordDTO recordDTO) {
        try {
            // 验证参数
            if (recordDTO.getTitle() == null || recordDTO.getTitle().trim().isEmpty()) {
                return ApiResponse.error("标题不能为空");
            }
            if (recordDTO.getVideos() == null || recordDTO.getVideos().isEmpty()) {
                return ApiResponse.error("视频文件不能为空");
            }
            
            // 创建记录
            VideoRecord record = new VideoRecord();
            record.setTitle(recordDTO.getTitle());
            record.setDescription(recordDTO.getDescription());
            record.setCreateTime(LocalDateTime.now());
            record.setUpdateTime(LocalDateTime.now());
            
            // 插入记录
            videoRecordMapper.insert(record);
            Long recordId = record.getId();
            
            // 插入视频文件
            for (VideoFileDTO videoDTO : recordDTO.getVideos()) {
                VideoFile videoFile = new VideoFile();
                videoFile.setRecordId(recordId);
                videoFile.setVideoData(videoDTO.getVideoData());
                videoFile.setName(videoDTO.getName());
                videoFile.setSize(videoDTO.getSize());
                videoFile.setType(videoDTO.getType());
                videoFileMapper.insert(videoFile);
            }
            
            // 查询完整的记录
            VideoRecord createdRecord = videoRecordMapper.selectById(recordId);
            VideoRecordDTO resultDTO = convertToDTO(createdRecord);
            
            return ApiResponse.success("创建视频记录成功", resultDTO);
        } catch (Exception e) {
            return ApiResponse.error("创建视频记录失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public ApiResponse updateVideoRecord(Long id, VideoRecordDTO recordDTO) {
        try {
            // 验证记录是否存在
            VideoRecord existingRecord = videoRecordMapper.selectById(id);
            if (existingRecord == null) {
                return ApiResponse.error("视频记录不存在");
            }
            
            // 验证参数
            if (recordDTO.getTitle() == null || recordDTO.getTitle().trim().isEmpty()) {
                return ApiResponse.error("标题不能为空");
            }
            if (recordDTO.getVideos() == null || recordDTO.getVideos().isEmpty()) {
                return ApiResponse.error("视频文件不能为空");
            }
            
            // 更新记录
            existingRecord.setTitle(recordDTO.getTitle());
            existingRecord.setDescription(recordDTO.getDescription());
            existingRecord.setUpdateTime(LocalDateTime.now());
            
            videoRecordMapper.update(existingRecord);
            
            // 删除旧的视频文件
            videoFileMapper.deleteByRecordId(id);
            
            // 插入新的视频文件
            for (VideoFileDTO videoDTO : recordDTO.getVideos()) {
                VideoFile videoFile = new VideoFile();
                videoFile.setRecordId(id);
                videoFile.setVideoData(videoDTO.getVideoData());
                videoFile.setName(videoDTO.getName());
                videoFile.setSize(videoDTO.getSize());
                videoFile.setType(videoDTO.getType());
                videoFileMapper.insert(videoFile);
            }
            
            // 查询更新后的记录
            VideoRecord updatedRecord = videoRecordMapper.selectById(id);
            VideoRecordDTO resultDTO = convertToDTO(updatedRecord);
            
            return ApiResponse.success("更新视频记录成功", resultDTO);
        } catch (Exception e) {
            return ApiResponse.error("更新视频记录失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public ApiResponse deleteVideoRecord(Long id) {
        try {
            // 验证记录是否存在
            VideoRecord existingRecord = videoRecordMapper.selectById(id);
            if (existingRecord == null) {
                return ApiResponse.error("视频记录不存在");
            }
            
            // 删除视频文件
            videoFileMapper.deleteByRecordId(id);
            
            // 删除记录
            videoRecordMapper.deleteById(id);
            
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error("删除视频记录失败: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse getVideoRecord(Long id) {
        try {
            VideoRecord record = videoRecordMapper.selectById(id);
            if (record == null) {
                return ApiResponse.error("视频记录不存在");
            }
            
            VideoRecordDTO resultDTO = convertToDTO(record);
            return ApiResponse.success("获取视频记录成功", resultDTO);
        } catch (Exception e) {
            return ApiResponse.error("获取视频记录失败: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse uploadVideo(byte[] videoData, String fileName, String contentType) {
        try {
            // TODO: 您需要在这里实现视频保存逻辑
            // 这里只是示例，您可以根据需要实现具体的保存逻辑
            
            // 示例：将视频数据转换为base64字符串
            String videoDataBase64 = java.util.Base64.getEncoder().encodeToString(videoData);
            
            // 创建视频文件DTO
            VideoFileDTO videoFileDTO = new VideoFileDTO();
            videoFileDTO.setVideoData("data:" + contentType + ";base64," + videoDataBase64);
            videoFileDTO.setName(fileName);
            videoFileDTO.setSize((long) videoData.length);
            videoFileDTO.setType(contentType);
            
            return ApiResponse.success("上传视频成功", videoFileDTO);
        } catch (Exception e) {
            return ApiResponse.error("上传视频失败: " + e.getMessage());
        }
    }
    
    /**
     * 将实体转换为DTO
     */
    private VideoRecordDTO convertToDTO(VideoRecord record) {
        VideoRecordDTO dto = new VideoRecordDTO();
        dto.setId(record.getId());
        dto.setTitle(record.getTitle());
        dto.setDescription(record.getDescription());
        dto.setCreateTime(record.getCreateTime());
        dto.setUpdateTime(record.getUpdateTime());
        
        // 查询视频文件
        List<VideoFile> videos = videoFileMapper.selectByRecordId(record.getId());
        List<VideoFileDTO> videoDTOs = new ArrayList<>();
        for (VideoFile video : videos) {
            VideoFileDTO videoDTO = new VideoFileDTO();
            videoDTO.setId(video.getId());
            videoDTO.setVideoData(video.getVideoData());
            videoDTO.setName(video.getName());
            videoDTO.setSize(video.getSize());
            videoDTO.setType(video.getType());
            videoDTOs.add(videoDTO);
        }
        dto.setVideos(videoDTOs);
        
        return dto;
    }
} 