package com.example.service;

import com.example.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {
    
    /**
     * 查询所有分类
     */
    List<Category> getAllCategories();
    
    /**
     * 根据ID查询分类
     */
    Category getCategoryById(Integer id);
    
    /**
     * 添加分类
     */
    boolean addCategory(Category category);
    
    /**
     * 更新分类
     */
    boolean updateCategory(Category category);
    
    /**
     * 删除分类
     */
    boolean deleteCategory(Integer id);
    
    /**
     * 根据名称查询分类
     */
    Category getCategoryByName(String name);
} 