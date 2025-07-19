package com.tool.service;

import com.tool.entity.Content;
import com.tool.dto.ContentDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 内容服务类
 */
@Service
public class ContentService {
    
    // 模拟数据库存储
    private final Map<Long, Content> contentMap = new HashMap<>();
    private Long nextId = 1L;

    public ContentService() {
        // 初始化一些测试数据
        initializeTestData();
    }

    /**
     * 搜索内容
     */
    public List<ContentDTO> searchContent(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerKeyword = keyword.toLowerCase();
        
        return contentMap.values().stream()
                .filter(content -> 
                    content.getTitle().toLowerCase().contains(lowerKeyword) ||
                    content.getKeywords().toLowerCase().contains(lowerKeyword) ||
                    content.getDescription().toLowerCase().contains(lowerKeyword) ||
                    content.getContent().toLowerCase().contains(lowerKeyword)
                )
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID获取内容
     */
    public ContentDTO getContentById(Long id) {
        Content content = contentMap.get(id);
        return content != null ? convertToDTO(content) : null;
    }

    /**
     * 创建新内容
     */
    public ContentDTO createContent(ContentDTO contentDTO) {
        Content content = new Content();
        content.setId(nextId++);
        content.setTitle(contentDTO.getTitle());
        content.setKeywords(contentDTO.getKeywords());
        content.setDescription(contentDTO.getDescription());
        content.setVideoPath(contentDTO.getVideoPath());
        content.setImagePath(contentDTO.getImagePath());
        content.setContent(contentDTO.getContent());
        content.setCreatedAt(LocalDateTime.now());
        content.setUpdatedAt(LocalDateTime.now());

        contentMap.put(content.getId(), content);
        return convertToDTO(content);
    }

    /**
     * 更新内容
     */
    public ContentDTO updateContent(Long id, ContentDTO contentDTO) {
        Content existingContent = contentMap.get(id);
        if (existingContent == null) {
            return null;
        }

        existingContent.setTitle(contentDTO.getTitle());
        existingContent.setKeywords(contentDTO.getKeywords());
        existingContent.setDescription(contentDTO.getDescription());
        existingContent.setVideoPath(contentDTO.getVideoPath());
        existingContent.setImagePath(contentDTO.getImagePath());
        existingContent.setContent(contentDTO.getContent());
        existingContent.setUpdatedAt(LocalDateTime.now());

        contentMap.put(id, existingContent);
        return convertToDTO(existingContent);
    }

    /**
     * 删除内容
     */
    public boolean deleteContent(Long id) {
        return contentMap.remove(id) != null;
    }

    /**
     * 获取所有内容
     */
    public List<ContentDTO> getAllContent() {
        return contentMap.values().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 实体转DTO
     */
    private ContentDTO convertToDTO(Content content) {
        ContentDTO dto = new ContentDTO();
        dto.setId(content.getId());
        dto.setTitle(content.getTitle());
        dto.setKeywords(content.getKeywords());
        dto.setDescription(content.getDescription());
        dto.setVideoPath(content.getVideoPath());
        dto.setImagePath(content.getImagePath());
        dto.setContent(content.getContent());
        dto.setCreatedAt(content.getCreatedAt());
        dto.setUpdatedAt(content.getUpdatedAt());
        return dto;
    }

    /**
     * 初始化测试数据
     */
    private void initializeTestData() {
        // 添加测试数据
        Content content1 = new Content();
        content1.setId(nextId++);
        content1.setTitle("系统登录操作指南");
        content1.setKeywords("登录,系统,用户名,密码");
        content1.setDescription("详细说明如何登录企业系统，包括用户名密码设置等");
        content1.setVideoPath("/uploads/videos/login-guide.mp4");
        content1.setImagePath("/uploads/images/login-steps.jpg");
        content1.setContent("1. 打开浏览器\n2. 输入系统地址\n3. 输入用户名和密码\n4. 点击登录按钮");
        content1.setCreatedAt(LocalDateTime.now());
        content1.setUpdatedAt(LocalDateTime.now());

        Content content2 = new Content();
        content2.setId(nextId++);
        content2.setTitle("文件上传操作步骤");
        content2.setKeywords("文件,上传,管理");
        content2.setDescription("介绍如何在系统中上传和管理文件");
        content2.setVideoPath("/uploads/videos/file-upload.mp4");
        content2.setImagePath("/uploads/images/upload-steps.jpg");
        content2.setContent("1. 选择要上传的文件\n2. 点击上传按钮\n3. 等待上传完成\n4. 确认文件已上传");
        content2.setCreatedAt(LocalDateTime.now());
        content2.setUpdatedAt(LocalDateTime.now());

        contentMap.put(content1.getId(), content1);
        contentMap.put(content2.getId(), content2);
    }
} 