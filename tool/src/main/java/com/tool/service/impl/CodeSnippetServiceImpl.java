package com.tool.service.impl;

import com.tool.dto.*;
import com.tool.entity.CodeSnippetRecord;
import com.tool.entity.CodeSnippetTag;
import com.tool.mapper.CodeSnippetRecordMapper;
import com.tool.mapper.CodeSnippetTagMapper;
import com.tool.service.CodeSnippetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码片段服务实现类
 * 
 * @author tool-service
 * @version 1.0.0
 */
@Service
public class CodeSnippetServiceImpl implements CodeSnippetService {
    
    @Autowired
    private CodeSnippetRecordMapper codeSnippetRecordMapper;
    
    @Autowired
    private CodeSnippetTagMapper codeSnippetTagMapper;
    
    @Override
    public ApiResponse getRecords(Integer page, Integer size, String title, String language) {
        try {
            // 设置默认值
            page = page == null ? 1 : page;
            size = size == null ? 20 : size;
            int offset = (page - 1) * size;
            
            List<CodeSnippetRecord> records;
            int total;
            
            // 根据条件查询
            if (title != null && !title.trim().isEmpty()) {
                records = codeSnippetRecordMapper.selectByTitle(title.trim());
                total = codeSnippetRecordMapper.countByTitle(title.trim());
            } else if (language != null && !language.trim().isEmpty()) {
                records = codeSnippetRecordMapper.selectByLanguage(language.trim());
                total = codeSnippetRecordMapper.count(); // 简化处理，实际可以添加按语言统计的方法
            } else {
                records = codeSnippetRecordMapper.selectByPage(offset, size);
                total = codeSnippetRecordMapper.count();
            }
            
            // 转换为DTO
            List<CodeSnippetRecordDTO> recordDTOs = new ArrayList<>();
            for (CodeSnippetRecord record : records) {
                // 查询每个记录的标签
                List<CodeSnippetTag> tags = codeSnippetTagMapper.selectByRecordId(record.getId());
                record.setTags(tags);
                CodeSnippetRecordDTO dto = convertToDTO(record);
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
            List<CodeSnippetRecord> records = codeSnippetRecordMapper.selectByKeyword(query.trim());
            int total = codeSnippetRecordMapper.countByKeyword(query.trim());
            
            // 转换为DTO
            List<CodeSnippetRecordDTO> recordDTOs = new ArrayList<>();
            for (CodeSnippetRecord record : records) {
                // 查询每个记录的标签
                List<CodeSnippetTag> tags = codeSnippetTagMapper.selectByRecordId(record.getId());
                record.setTags(tags);
                CodeSnippetRecordDTO dto = convertToDTO(record);
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
    public ApiResponse createRecord(CreateCodeSnippetRequest request) {
        try {
            // 验证参数
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                return ApiResponse.error("标题不能为空");
            }
            if (request.getCode() == null || request.getCode().trim().isEmpty()) {
                return ApiResponse.error("代码内容不能为空");
            }
            
            // 创建记录
            CodeSnippetRecord record = new CodeSnippetRecord();
            record.setTitle(request.getTitle());
            record.setDescription(request.getDescription());
            record.setCode(request.getCode());
            record.setLanguage(request.getLanguage());
            record.setCreatedAt(LocalDateTime.now());
            record.setUpdatedAt(LocalDateTime.now());
            
            // 插入记录
            codeSnippetRecordMapper.insert(record);
            Long recordId = record.getId();
            
            // 插入标签
            if (request.getTags() != null && !request.getTags().isEmpty()) {
                List<CodeSnippetTag> tags = new ArrayList<>();
                for (String tagName : request.getTags()) {
                    if (tagName != null && !tagName.trim().isEmpty()) {
                        CodeSnippetTag tag = new CodeSnippetTag();
                        tag.setRecordId(recordId);
                        tag.setTagName(tagName.trim());
                        tag.setCreatedAt(LocalDateTime.now());
                        tags.add(tag);
                    }
                }
                if (!tags.isEmpty()) {
                    codeSnippetTagMapper.insertBatch(tags);
                }
            }
            
            // 查询完整的记录
            CodeSnippetRecord createdRecord = codeSnippetRecordMapper.selectWithTags(recordId);
            CodeSnippetRecordDTO resultDTO = convertToDTO(createdRecord);
            
            return ApiResponse.success("创建记录成功", resultDTO);
        } catch (Exception e) {
            return ApiResponse.error("创建记录失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public ApiResponse updateRecord(Long id, CreateCodeSnippetRequest request) {
        try {
            // 验证参数
            if (id == null) {
                return ApiResponse.error("记录ID不能为空");
            }
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                return ApiResponse.error("标题不能为空");
            }
            if (request.getCode() == null || request.getCode().trim().isEmpty()) {
                return ApiResponse.error("代码内容不能为空");
            }
            
            // 检查记录是否存在
            CodeSnippetRecord existingRecord = codeSnippetRecordMapper.selectById(id);
            if (existingRecord == null) {
                return ApiResponse.error("记录不存在");
            }
            
            // 更新记录
            existingRecord.setTitle(request.getTitle());
            existingRecord.setDescription(request.getDescription());
            existingRecord.setCode(request.getCode());
            existingRecord.setLanguage(request.getLanguage());
            existingRecord.setUpdatedAt(LocalDateTime.now());
            
            codeSnippetRecordMapper.update(existingRecord);
            
            // 删除旧标签
            codeSnippetTagMapper.deleteByRecordId(id);
            
            // 插入新标签
            if (request.getTags() != null && !request.getTags().isEmpty()) {
                List<CodeSnippetTag> tags = new ArrayList<>();
                for (String tagName : request.getTags()) {
                    if (tagName != null && !tagName.trim().isEmpty()) {
                        CodeSnippetTag tag = new CodeSnippetTag();
                        tag.setRecordId(id);
                        tag.setTagName(tagName.trim());
                        tag.setCreatedAt(LocalDateTime.now());
                        tags.add(tag);
                    }
                }
                if (!tags.isEmpty()) {
                    codeSnippetTagMapper.insertBatch(tags);
                }
            }
            
            // 查询完整的记录
            CodeSnippetRecord updatedRecord = codeSnippetRecordMapper.selectWithTags(id);
            CodeSnippetRecordDTO resultDTO = convertToDTO(updatedRecord);
            
            return ApiResponse.success("更新记录成功", resultDTO);
        } catch (Exception e) {
            return ApiResponse.error("更新记录失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public ApiResponse deleteRecord(Long id) {
        try {
            if (id == null) {
                return ApiResponse.error("记录ID不能为空");
            }
            
            // 检查记录是否存在
            CodeSnippetRecord existingRecord = codeSnippetRecordMapper.selectById(id);
            if (existingRecord == null) {
                return ApiResponse.error("记录不存在");
            }
            
            // 删除关联的标签
            codeSnippetTagMapper.deleteByRecordId(id);
            
            // 删除记录
            codeSnippetRecordMapper.deleteById(id);
            
            return ApiResponse.success("删除记录成功", null);
        } catch (Exception e) {
            return ApiResponse.error("删除记录失败: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse getRecordById(Long id) {
        try {
            if (id == null) {
                return ApiResponse.error("记录ID不能为空");
            }
            
            CodeSnippetRecord record = codeSnippetRecordMapper.selectWithTags(id);
            if (record == null) {
                return ApiResponse.error("记录不存在");
            }
            
            CodeSnippetRecordDTO dto = convertToDTO(record);
            return ApiResponse.success("获取记录成功", dto);
        } catch (Exception e) {
            return ApiResponse.error("获取记录失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public ApiResponse cleanOrphanTags() {
        try {
            int deletedCount = codeSnippetTagMapper.deleteOrphanTags();
            Map<String, Object> result = new HashMap<>();
            result.put("deletedCount", deletedCount);
            return ApiResponse.success("清理孤立标签成功", result);
        } catch (Exception e) {
            return ApiResponse.error("清理孤立标签失败: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse getAllTagNames() {
        try {
            List<String> tagNames = codeSnippetTagMapper.selectDistinctTagNames();
            return ApiResponse.success("获取标签列表成功", tagNames);
        } catch (Exception e) {
            return ApiResponse.error("获取标签列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 将实体转换为DTO
     */
    private CodeSnippetRecordDTO convertToDTO(CodeSnippetRecord record) {
        CodeSnippetRecordDTO dto = new CodeSnippetRecordDTO();
        dto.setId(record.getId());
        dto.setTitle(record.getTitle());
        dto.setDescription(record.getDescription());
        dto.setCode(record.getCode());
        dto.setLanguage(record.getLanguage());
        dto.setCreatedAt(record.getCreatedAt());
        dto.setUpdatedAt(record.getUpdatedAt());
        
        // 转换标签
        if (record.getTags() != null) {
            List<CodeSnippetTagDTO> tagDTOs = new ArrayList<>();
            for (CodeSnippetTag tag : record.getTags()) {
                CodeSnippetTagDTO tagDTO = new CodeSnippetTagDTO();
                tagDTO.setId(tag.getId());
                tagDTO.setRecordId(tag.getRecordId());
                tagDTO.setTagName(tag.getTagName());
                tagDTO.setCreatedAt(tag.getCreatedAt());
                tagDTOs.add(tagDTO);
            }
            dto.setTags(tagDTOs);
        }
        
        return dto;
    }
}