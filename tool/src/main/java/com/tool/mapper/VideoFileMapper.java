package com.tool.mapper;

import com.tool.entity.VideoFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 视频文件Mapper接口
 */
@Mapper
public interface VideoFileMapper {
    
    /**
     * 插入视频文件
     */
    int insert(VideoFile videoFile);
    
    /**
     * 根据ID查询视频文件
     */
    VideoFile selectById(@Param("id") Long id);
    
    /**
     * 根据记录ID查询视频文件列表
     */
    List<VideoFile> selectByRecordId(@Param("recordId") Long recordId);
    
    /**
     * 更新视频文件
     */
    int update(VideoFile videoFile);
    
    /**
     * 根据ID删除视频文件
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据记录ID删除视频文件
     */
    int deleteByRecordId(@Param("recordId") Long recordId);
} 