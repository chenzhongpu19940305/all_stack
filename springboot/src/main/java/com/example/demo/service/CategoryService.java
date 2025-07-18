package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    // 获取所有分类
    public List<Category> getAllCategories() {
        return categoryMapper.findAll();
    }
    
    // 根据ID获取分类
    public Category getCategoryById(Long id) {
        return categoryMapper.findById(id);
    }
    
    // 根据代码获取分类
    public Category getCategoryByCode(String code) {
        return categoryMapper.findByCode(code);
    }
    
    // 创建分类
    public Category createCategory(Category category) {
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        categoryMapper.insert(category);
        return category;
    }
    
    // 更新分类
    public Category updateCategory(Category category) {
        category.setUpdatedAt(LocalDateTime.now());
        categoryMapper.update(category);
        return category;
    }
    
    // 删除分类
    public boolean deleteCategory(Long id) {
        return categoryMapper.deleteById(id) > 0;
    }
    
    // 获取前N个分类
    public List<Category> getTopCategories(int limit) {
        return categoryMapper.findTopCategories(limit);
    }
    
    // 搜索分类
    public List<Category> searchCategories(String keyword) {
        return categoryMapper.searchCategories(keyword);
    }
} 