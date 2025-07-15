package com.example.demo.repository;

import com.example.demo.entity.Algorithm;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Mapper
public interface AlgorithmRepository {
    
    @Select("SELECT * FROM algorithms")
    List<Algorithm> findAll();
    
    @Select("SELECT * FROM algorithms WHERE id = #{id}")
    Algorithm findById(Long id);
    
    @Select("SELECT * FROM algorithms WHERE category = #{category}")
    List<Algorithm> findByCategory(String category);
    
    @Select("SELECT DISTINCT category FROM algorithms")
    List<String> findAllCategories();
    
    @Select("SELECT * FROM algorithms WHERE name LIKE CONCAT('%', #{name}, '%')")
    List<Algorithm> findByNameContainingIgnoreCase(String name);
    
    @Insert("INSERT INTO algorithms(name, category, description, code, created_at, updated_at) " +
            "VALUES(#{name}, #{category}, #{description}, #{code}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Algorithm algorithm);
    
    @Update("UPDATE algorithms SET name = #{name}, category = #{category}, " +
            "description = #{description}, code = #{code}, updated_at = #{updatedAt} " +
            "WHERE id = #{id}")
    int update(Algorithm algorithm);
    
    @Delete("DELETE FROM algorithms WHERE id = #{id}")
    int deleteById(Long id);
} 