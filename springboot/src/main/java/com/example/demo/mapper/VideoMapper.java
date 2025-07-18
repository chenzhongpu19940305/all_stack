package com.example.demo.mapper;

import com.example.demo.entity.Video;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VideoMapper {
    
    // 查询所有视频
    @Select("SELECT * FROM videos ORDER BY created_at DESC")
    List<Video> findAll();
    
    // 根据ID查询视频
    @Select("SELECT * FROM videos WHERE id = #{id}")
    Video findById(Long id);
    
    // 根据分类查询视频
    @Select("SELECT * FROM videos WHERE category = #{category} ORDER BY created_at DESC")
    List<Video> findByCategory(String category);
    
    // 搜索视频
    @Select("SELECT * FROM videos WHERE title LIKE CONCAT('%', #{keyword}, '%') OR author LIKE CONCAT('%', #{keyword}, '%') ORDER BY created_at DESC")
    List<Video> searchVideos(String keyword);
    
    // 获取热门视频（按播放量排序）
    @Select("SELECT * FROM videos ORDER BY views DESC LIMIT #{limit}")
    List<Video> findPopularVideos(int limit);
    
    // 获取最新视频
    @Select("SELECT * FROM videos ORDER BY created_at DESC LIMIT #{limit}")
    List<Video> findLatestVideos(int limit);
    
    // 插入视频
    @Insert("INSERT INTO videos (title, author, description, thumbnail, video_url, duration, category, views, likes, upload_time, created_at, updated_at) " +
            "VALUES (#{title}, #{author}, #{description}, #{thumbnail}, #{videoUrl}, #{duration}, #{category}, #{views}, #{likes}, #{uploadTime}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Video video);
    
    // 更新视频
    @Update("UPDATE videos SET title = #{title}, author = #{author}, description = #{description}, " +
            "thumbnail = #{thumbnail}, video_url = #{videoUrl}, duration = #{duration}, " +
            "category = #{category}, views = #{views}, likes = #{likes}, " +
            "upload_time = #{uploadTime}, updated_at = #{updatedAt} WHERE id = #{id}")
    int update(Video video);
    
    // 删除视频
    @Delete("DELETE FROM videos WHERE id = #{id}")
    int deleteById(Long id);
    
    // 增加播放量
    @Update("UPDATE videos SET views = views + 1, updated_at = NOW() WHERE id = #{id}")
    int incrementViews(Long id);
    
    // 增加点赞数
    @Update("UPDATE videos SET likes = likes + 1, updated_at = NOW() WHERE id = #{id}")
    int incrementLikes(Long id);
    
    // 减少点赞数
    @Update("UPDATE videos SET likes = likes - 1, updated_at = NOW() WHERE id = #{id}")
    int decrementLikes(Long id);
    
    // 根据播放量范围查询
    @Select("SELECT * FROM videos WHERE views BETWEEN #{minViews} AND #{maxViews} ORDER BY views DESC")
    List<Video> findByViewsRange(Long minViews, Long maxViews);
    
    // 根据上传时间范围查询
    @Select("SELECT * FROM videos WHERE upload_time BETWEEN #{startTime} AND #{endTime} ORDER BY upload_time DESC")
    List<Video> findByUploadTimeRange(String startTime, String endTime);
} 