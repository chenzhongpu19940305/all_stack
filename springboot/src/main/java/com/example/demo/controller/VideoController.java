package com.example.demo.controller;

import com.example.demo.entity.Video;
import com.example.demo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/videos")
@CrossOrigin(origins = "*")
public class VideoController {
    
    @Autowired
    private VideoService videoService;
    
    // 获取所有视频
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVideos() {
        try {
            List<Video> videos = videoService.getAllVideos();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", videos);
            response.put("message", "获取视频列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取视频列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 根据ID获取视频
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getVideoById(@PathVariable Long id) {
        try {
            Video video = videoService.getVideoById(id);
            if (video != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", video);
                response.put("message", "获取视频成功");
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "视频不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取视频失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 根据分类获取视频
    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getVideosByCategory(@PathVariable String category) {
        try {
            List<Video> videos = videoService.getVideosByCategory(category);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", videos);
            response.put("message", "获取分类视频成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取分类视频失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 搜索视频
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchVideos(@RequestParam String keyword) {
        try {
            List<Video> videos = videoService.searchVideos(keyword);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", videos);
            response.put("message", "搜索视频成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "搜索视频失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 获取热门视频
    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getPopularVideos(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Video> videos = videoService.getPopularVideos(limit);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", videos);
            response.put("message", "获取热门视频成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取热门视频失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 获取最新视频
    @GetMapping("/latest")
    public ResponseEntity<Map<String, Object>> getLatestVideos(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Video> videos = videoService.getLatestVideos(limit);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", videos);
            response.put("message", "获取最新视频成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取最新视频失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 创建视频
    @PostMapping
    public ResponseEntity<Map<String, Object>> createVideo(@RequestBody Video video) {
        try {
            Video createdVideo = videoService.createVideo(video);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", createdVideo);
            response.put("message", "创建视频成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "创建视频失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 更新视频
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateVideo(@PathVariable Long id, @RequestBody Video video) {
        try {
            video.setId(id);
            Video updatedVideo = videoService.updateVideo(video);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updatedVideo);
            response.put("message", "更新视频成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新视频失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 删除视频
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteVideo(@PathVariable Long id) {
        try {
            boolean deleted = videoService.deleteVideo(id);
            Map<String, Object> response = new HashMap<>();
            if (deleted) {
                response.put("success", true);
                response.put("message", "删除视频成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "视频不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除视频失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 增加播放量
    @PostMapping("/{id}/views")
    public ResponseEntity<Map<String, Object>> incrementViews(@PathVariable Long id) {
        try {
            boolean success = videoService.incrementViews(id);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "增加播放量成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "视频不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "增加播放量失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 增加点赞数
    @PostMapping("/{id}/likes")
    public ResponseEntity<Map<String, Object>> incrementLikes(@PathVariable Long id) {
        try {
            boolean success = videoService.incrementLikes(id);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "增加点赞数成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "视频不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "增加点赞数失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 减少点赞数
    @DeleteMapping("/{id}/likes")
    public ResponseEntity<Map<String, Object>> decrementLikes(@PathVariable Long id) {
        try {
            boolean success = videoService.decrementLikes(id);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "减少点赞数成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "视频不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "减少点赞数失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 