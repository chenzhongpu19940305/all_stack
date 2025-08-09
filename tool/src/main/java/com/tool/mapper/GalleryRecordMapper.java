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
     * 根据ID查询记录
     */
    GalleryRecord selectById(@Param("id") Long id);
    
    /**
     * 查询所有记录
     */
    List<GalleryRecord> selectAll();
    
    /**
     * 分页查询记录
     */
    List<GalleryRecord> selectByPage(@Param("offset") int offset, @Param("size") int size);
    
    /**
     * 根据标题搜索记录
     */
    List<GalleryRecord> selectByTitle(@Param("title") String title);
    
    /**
     * 统计记录总数
     */
    int count();
    
    /**
     * 根据标题统计记录数
     */
    int countByTitle(@Param("title") String title);
    
    /**
     * 插入记录
     */
    int insert(GalleryRecord record);
    
    /**
     * 更新记录
     */
    int update(GalleryRecord record);
    
    /**
     * 根据ID删除记录
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 查询记录及其关联的图片
     */
    GalleryRecord selectWithImages(@Param("id") Long id);
    
    /**
     * 分页查询记录及其关联的图片
     */
    List<GalleryRecord> selectWithImagesByPage(@Param("offset") int offset, @Param("size") int size);
}