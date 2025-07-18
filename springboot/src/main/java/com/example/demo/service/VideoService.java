package com.example.demo.service;

import com.example.demo.entity.Video;
import com.example.demo.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VideoService {
    
    @Autowired
    private VideoMapper videoMapper;
    
    // 获取所有视频
    public List<Video> getAllVideos() {
        return videoMapper.findAll();
    }
    
    // 根据ID获取视频
    public Video getVideoById(Long id) {
        return videoMapper.findById(id);
    }
    
    // 根据分类获取视频
    public List<Video> getVideosByCategory(String category) {
        return videoMapper.findByCategory(category);
    }
    
    // 搜索视频
    public List<Video> searchVideos(String keyword) {
        return videoMapper.searchVideos(keyword);
    }
    
    // 获取热门视频
    public List<Video> getPopularVideos(int limit) {
        return videoMapper.findPopularVideos(limit);
    }
    
    // 获取最新视频
    public List<Video> getLatestVideos(int limit) {
        return videoMapper.findLatestVideos(limit);
    }
    
    // 创建视频
    public Video createVideo(Video video) {
        video.setCreatedAt(LocalDateTime.now());
        video.setUpdatedAt(LocalDateTime.now());
        video.setUploadTime(LocalDateTime.now());
        videoMapper.insert(video);
        return video;
    }
    
    // 更新视频
    public Video updateVideo(Video video) {
        video.setUpdatedAt(LocalDateTime.now());
        videoMapper.update(video);
        return video;
    }
    
    // 删除视频
    public boolean deleteVideo(Long id) {
        return videoMapper.deleteById(id) > 0;
    }
    
    // 增加播放量
    public boolean incrementViews(Long id) {
        return videoMapper.incrementViews(id) > 0;
    }
    
    // 增加点赞数
    public boolean incrementLikes(Long id) {
        return videoMapper.incrementLikes(id) > 0;
    }
    
    // 减少点赞数
    public boolean decrementLikes(Long id) {
        return videoMapper.decrementLikes(id) > 0;
    }
    
    // 根据播放量范围查询
    public List<Video> getVideosByViewsRange(Long minViews, Long maxViews) {
        return videoMapper.findByViewsRange(minViews, maxViews);
    }
    
    // 根据上传时间范围查询
    public List<Video> getVideosByUploadTimeRange(String startTime, String endTime) {
        return videoMapper.findByUploadTimeRange(startTime, endTime);
    }
} 