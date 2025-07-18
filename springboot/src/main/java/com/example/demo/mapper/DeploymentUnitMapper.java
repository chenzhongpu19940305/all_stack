package com.example.demo.mapper;

import com.example.demo.entity.DeploymentUnit;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeploymentUnitMapper {
    
    @Select("SELECT * FROM deployment_units WHERE status = 'enabled' ORDER BY name")
    List<DeploymentUnit> findAllEnabled();
    
    @Select("SELECT * FROM deployment_units WHERE id = #{id}")
    DeploymentUnit findById(Long id);
    
    @Select("SELECT * FROM deployment_units WHERE code = #{code}")
    DeploymentUnit findByCode(String code);
    
    @Insert("INSERT INTO deployment_units (name, code, description, status, created_at, updated_at) " +
            "VALUES (#{name}, #{code}, #{description}, #{status}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DeploymentUnit deploymentUnit);
    
    @Update("UPDATE deployment_units SET name = #{name}, code = #{code}, description = #{description}, " +
            "status = #{status}, updated_at = #{updatedAt} WHERE id = #{id}")
    int update(DeploymentUnit deploymentUnit);
    
    @Delete("DELETE FROM deployment_units WHERE id = #{id}")
    int deleteById(Long id);
} 