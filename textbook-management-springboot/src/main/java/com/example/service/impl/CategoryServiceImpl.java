package com.example.service.impl;

import com.example.dao.CategoryDao;
import com.example.entity.Category;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分类服务实现类
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.selectAll();
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryDao.selectById(id);
    }

    @Override
    public boolean addCategory(Category category) {
        return categoryDao.insert(category) > 0;
    }

    @Override
    public boolean updateCategory(Category category) {
        return categoryDao.update(category) > 0;
    }

    @Override
    public boolean deleteCategory(Integer id) {
        return categoryDao.deleteById(id) > 0;
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryDao.selectByName(name);
    }
} 