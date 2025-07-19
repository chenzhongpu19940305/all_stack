package com.tool.mapper;

import com.tool.entity.VideoRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 视频记录Mapper接口
 */
@Mapper
public interface VideoRecordMapper {
    
    /**
     * 插入视频记录
     */
    int insert(VideoRecord record);
    
    /**
     * 根据ID查询视频记录
     */
    VideoRecord selectById(@Param("id") Long id);
    
    /**
     * 查询所有视频记录
     */
    List<VideoRecord> selectAll();
    
    /**
     * 分页查询视频记录
     */
    List<VideoRecord> selectByPage(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 根据标题搜索视频记录
     */
    List<VideoRecord> selectByTitle(@Param("title") String title);
    
    /**
     * 更新视频记录
     */
    int update(VideoRecord record);
    
    /**
     * 根据ID删除视频记录
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 统计总记录数
     */
    int count();
    
    /**
     * 根据标题统计记录数
     */
    int countByTitle(@Param("title") String title);
} 