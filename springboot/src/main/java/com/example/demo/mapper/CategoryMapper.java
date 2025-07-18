package com.example.demo.mapper;

import com.example.demo.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    
    // 查询所有分类
    @Select("SELECT * FROM categories WHERE active = true ORDER BY sort_order ASC")
    List<Category> findAll();
    
    // 根据ID查询分类
    @Select("SELECT * FROM categories WHERE id = #{id}")
    Category findById(Long id);
    
    // 根据代码查询分类
    @Select("SELECT * FROM categories WHERE code = #{code}")
    Category findByCode(String code);
    
    // 插入分类
    @Insert("INSERT INTO categories (name, code, description, icon_url, sort_order, active, created_at, updated_at) " +
            "VALUES (#{name}, #{code}, #{description}, #{iconUrl}, #{sortOrder}, #{active}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Category category);
    
    // 更新分类
    @Update("UPDATE categories SET name = #{name}, code = #{code}, description = #{description}, " +
            "icon_url = #{iconUrl}, sort_order = #{sortOrder}, active = #{active}, updated_at = #{updatedAt} WHERE id = #{id}")
    int update(Category category);
    
    // 删除分类
    @Delete("DELETE FROM categories WHERE id = #{id}")
    int deleteById(Long id);
    
    // 根据排序查询
    @Select("SELECT * FROM categories WHERE active = true ORDER BY sort_order ASC LIMIT #{limit}")
    List<Category> findTopCategories(int limit);
    
    // 根据名称模糊查询
    @Select("SELECT * FROM categories WHERE name LIKE CONCAT('%', #{keyword}, '%') AND active = true ORDER BY sort_order ASC")
    List<Category> searchCategories(String keyword);
} 