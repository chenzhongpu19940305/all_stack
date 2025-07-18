package com.example.demo.mapper;

import com.example.demo.entity.FeatureConfig;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FeatureConfigMapper {
    
    @Select("SELECT * FROM feature_configs WHERE feature_id = #{featureId} AND status = 'enabled' ORDER BY `key`")
    List<FeatureConfig> findByFeatureId(Long featureId);
    
    @Select("SELECT * FROM feature_configs WHERE id = #{id}")
    FeatureConfig findById(Long id);
    
    @Select("SELECT * FROM feature_configs WHERE feature_id = #{featureId} AND `key` = #{key}")
    FeatureConfig findByFeatureIdAndKey(Long featureId, String key);
    
    @Insert("INSERT INTO feature_configs (feature_id, `key`, value, description, status, created_at, updated_at) " +
            "VALUES (#{featureId}, #{key}, #{value}, #{description}, #{status}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FeatureConfig featureConfig);
    
    @Update("UPDATE feature_configs SET `key` = #{key}, value = #{value}, description = #{description}, " +
            "status = #{status}, updated_at = #{updatedAt} WHERE id = #{id}")
    int update(FeatureConfig featureConfig);
    
    @Delete("DELETE FROM feature_configs WHERE id = #{id}")
    int deleteById(Long id);
} 