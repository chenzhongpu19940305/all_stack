package com.example.demo.mapper;

import com.example.demo.entity.Feature;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FeatureMapper {
    
    @Select("SELECT * FROM features WHERE status = 'enabled' ORDER BY name")
    List<Feature> findAllEnabled();
    
    @Select("SELECT * FROM features WHERE application_code = #{applicationCode} AND deployment_unit_code = #{deploymentUnitCode} AND status = 'enabled' ORDER BY name")
    List<Feature> findByApplicationAndDeploymentUnit(String applicationCode, String deploymentUnitCode);
    
    @Select("SELECT * FROM features WHERE id = #{id}")
    Feature findById(Long id);
    
    @Select("SELECT * FROM features WHERE code = #{code}")
    Feature findByCode(String code);
    
    @Insert("INSERT INTO features (name, code, description, application_code, deployment_unit_code, image_url, status, created_at, updated_at) " +
            "VALUES (#{name}, #{code}, #{description}, #{applicationCode}, #{deploymentUnitCode}, #{imageUrl}, #{status}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Feature feature);
    
    @Update("UPDATE features SET name = #{name}, code = #{code}, description = #{description}, " +
            "application_code = #{applicationCode}, deployment_unit_code = #{deploymentUnitCode}, " +
            "image_url = #{imageUrl}, status = #{status}, updated_at = #{updatedAt} WHERE id = #{id}")
    int update(Feature feature);
    
    @Delete("DELETE FROM features WHERE id = #{id}")
    int deleteById(Long id);
} 