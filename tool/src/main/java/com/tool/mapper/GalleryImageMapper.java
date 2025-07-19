package com.tool.mapper;

import com.tool.entity.GalleryImage;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Gallery图片Mapper接口
 * 
 * @author tool-service
 * @version 1.0.0
 */
@Mapper
public interface GalleryImageMapper {
    
    /**
     * 根据记录ID获取图片
     * 
     * @param recordId 记录ID
     * @return 图片列表
     */
    List<GalleryImage> selectByRecordId(@Param("recordId") Long recordId);
    
    /**
     * 插入新图片
     * 
     * @param image 图片
     * @return 影响的行数
     */
    int insert(GalleryImage image);
    
    /**
     * 根据ID删除图片
     * 
     * @param id 图片ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据记录ID删除图片
     * 
     * @param recordId 记录ID
     * @return 影响的行数
     */
    int deleteByRecordId(@Param("recordId") Long recordId);
} 