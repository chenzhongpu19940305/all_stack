package com.example.demo.mapper;

import com.example.demo.entity.GalleryImage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GalleryImageMapper {
    
    @Select("SELECT * FROM gallery_images WHERE record_id = #{recordId}")
    List<GalleryImage> findByRecordId(Long recordId);
    
    @Insert("INSERT INTO gallery_images (name, content_type, image_data, record_id) VALUES (#{name}, #{contentType}, #{imageData}, #{recordId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(GalleryImage image);
    
    @Delete("DELETE FROM gallery_images WHERE record_id = #{recordId}")
    int deleteByRecordId(Long recordId);
    
    @Delete("DELETE FROM gallery_images WHERE id = #{id}")
    int deleteById(Long id);
    
    @Select("SELECT * FROM gallery_images WHERE id = #{id}")
    GalleryImage findById(Long id);
    
    @Update("UPDATE gallery_images SET record_id = #{recordId} WHERE id = #{id}")
    int updateRecordId(Long id, Long recordId);
} 