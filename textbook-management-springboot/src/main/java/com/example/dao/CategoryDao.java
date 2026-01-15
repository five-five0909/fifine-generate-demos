package com.example.dao;

import com.example.entity.Category;

import java.util.List;

/**
 * 分类数据访问接口
 */
public interface CategoryDao {
    
    /**
     * 查询所有分类
     */
    List<Category> selectAll();
    
    /**
     * 根据ID查询分类
     */
    Category selectById(Integer id);
    
    /**
     * 添加分类
     */
    int insert(Category category);
    
    /**
     * 更新分类
     */
    int update(Category category);
    
    /**
     * 删除分类
     */
    int deleteById(Integer id);
    
    /**
     * 根据名称查询分类
     */
    Category selectByName(String name);
} 