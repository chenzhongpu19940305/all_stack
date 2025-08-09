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
     * 根据ID查询图片
     */
    GalleryImage selectById(@Param("id") Long id);
    
    /**
     * 根据记录ID查询图片列表
     */
    List<GalleryImage> selectByRecordId(@Param("recordId") Long recordId);
    
    /**
     * 查询所有图片
     */
    List<GalleryImage> selectAll();
    
    /**
     * 插入图片
     */
    int insert(GalleryImage image);
    
    /**
     * 更新图片
     */
    int update(GalleryImage image);
    
    /**
     * 根据ID删除图片
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据记录ID删除图片
     */
    int deleteByRecordId(@Param("recordId") Long recordId);
    
    /**
     * 批量插入图片
     */
    int insertBatch(@Param("images") List<GalleryImage> images);
    
    /**
     * 查询孤立图片（没有关联记录的图片）
     */
    List<GalleryImage> selectOrphanImages();
    
    /**
     * 删除孤立图片
     */
    int deleteOrphanImages();
}