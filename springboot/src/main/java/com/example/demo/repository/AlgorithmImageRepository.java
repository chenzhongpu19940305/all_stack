package com.example.demo.repository;

import com.example.demo.entity.AlgorithmImage;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Mapper
public interface AlgorithmImageRepository {
    
    @Select("SELECT * FROM algorithm_images WHERE algorithm_id = #{algorithmId} ORDER BY sort_order")
    List<AlgorithmImage> findByAlgorithmId(Long algorithmId);
    
    @Insert("INSERT INTO algorithm_images(algorithm_id, src, `desc`, sort_order, created_at) " +
            "VALUES(#{algorithmId}, #{src}, #{desc}, #{sortOrder}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AlgorithmImage image);
    
    @Update("UPDATE algorithm_images SET src = #{src}, `desc` = #{desc}, " +
            "sort_order = #{sortOrder} WHERE id = #{id}")
    int update(AlgorithmImage image);
    
    @Delete("DELETE FROM algorithm_images WHERE id = #{id}")
    int deleteById(Long id);
    
    @Delete("DELETE FROM algorithm_images WHERE algorithm_id = #{algorithmId}")
    int deleteByAlgorithmId(Long algorithmId);
} 