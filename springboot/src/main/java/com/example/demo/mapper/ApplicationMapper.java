package com.example.demo.mapper;

import com.example.demo.entity.Application;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ApplicationMapper {
    
    @Select("SELECT * FROM applications WHERE status = 'enabled' ORDER BY name")
    List<Application> findAllEnabled();
    
    @Select("SELECT * FROM applications WHERE id = #{id}")
    Application findById(Long id);
    
    @Select("SELECT * FROM applications WHERE code = #{code}")
    Application findByCode(String code);
    
    @Insert("INSERT INTO applications (name, code, description, status, created_at, updated_at) " +
            "VALUES (#{name}, #{code}, #{description}, #{status}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Application application);
    
    @Update("UPDATE applications SET name = #{name}, code = #{code}, description = #{description}, " +
            "status = #{status}, updated_at = #{updatedAt} WHERE id = #{id}")
    int update(Application application);
    
    @Delete("DELETE FROM applications WHERE id = #{id}")
    int deleteById(Long id);
} 