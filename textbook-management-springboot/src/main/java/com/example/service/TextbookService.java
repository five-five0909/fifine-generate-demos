package com.example.service;

import com.example.entity.Textbook;

import java.util.List;

/**
 * 教材服务接口
 */
public interface TextbookService {
    
    /**
     * 查询所有教材
     */
    List<Textbook> getAllTextbooks();
    
    /**
     * 根据ID查询教材
     */
    Textbook getTextbookById(Integer id);
    
    /**
     * 根据条件查询教材
     */
    List<Textbook> getTextbooksByCondition(String title, String author, Integer categoryId);
    
    /**
     * 添加教材
     */
    boolean addTextbook(Textbook textbook);
    
    /**
     * 更新教材
     */
    boolean updateTextbook(Textbook textbook);
    
    /**
     * 删除教材
     */
    boolean deleteTextbook(Integer id);
    
    /**
     * 更新库存
     */
    boolean updateStock(Integer id, Integer stock);
    
    /**
     * 统计教材总数
     */
    int getTextbookCount();
    
    /**
     * 分页查询
     */
    List<Textbook> getTextbooksByPage(int pageNum, int pageSize);
} 