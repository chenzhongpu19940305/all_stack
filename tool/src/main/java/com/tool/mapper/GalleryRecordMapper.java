package com.tool.mapper;

import com.tool.entity.GalleryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Gallery记录Mapper接口
 * 
 * @author tool-service
 * @version 1.0.0
 */
@Mapper
public interface GalleryRecordMapper {
    
    /**
     * 获取所有记录
     * 
     * @return 记录列表
     */
    List<GalleryRecord> selectAll();
    
    /**
     * 根据ID获取记录
     * 
     * @param id 记录ID
     * @return 记录
     */
    GalleryRecord selectById(@Param("id") Long id);
    
    /**
     * 根据标题搜索记录
     * 
     * @param title 标题关键词
     * @return 匹配的记录列表
     */
    List<GalleryRecord> selectByTitle(@Param("title") String title);
    
    /**
     * 插入新记录
     * 
     * @param record 记录
     * @return 影响的行数
     */
    int insert(GalleryRecord record);
    
    /**
     * 更新记录
     * 
     * @param record 记录
     * @return 影响的行数
     */
    int update(GalleryRecord record);
    
    /**
     * 删除记录
     * 
     * @param id 记录ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);
} 