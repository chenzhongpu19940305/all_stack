package com.example.demo.mapper;

import com.example.demo.entity.GalleryRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GalleryRecordMapper {
    
    @Select("SELECT * FROM gallery_records ORDER BY create_time DESC")
    List<GalleryRecord> findAll();
    
    @Select("SELECT * FROM gallery_records WHERE id = #{id}")
    GalleryRecord findById(Long id);
    
    @Select("SELECT * FROM gallery_records WHERE title LIKE CONCAT('%', #{query}, '%') ORDER BY create_time DESC")
    List<GalleryRecord> searchByTitle(String query);
    
    @Insert("INSERT INTO gallery_records (title, create_time, update_time) VALUES (#{title}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(GalleryRecord record);
    
    @Update("UPDATE gallery_records SET title = #{title}, update_time = NOW() WHERE id = #{id}")
    int update(GalleryRecord record);
    
    @Delete("DELETE FROM gallery_records WHERE id = #{id}")
    int deleteById(Long id);
} 