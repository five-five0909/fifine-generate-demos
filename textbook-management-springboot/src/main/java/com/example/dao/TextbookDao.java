package com.example.dao;

import com.example.entity.Textbook;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 教材数据访问接口
 */
public interface TextbookDao {
    
    /**
     * 查询所有教材
     */
    List<Textbook> selectAll();
    
    /**
     * 根据ID查询教材
     */
    Textbook selectById(Integer id);
    
    /**
     * 根据条件查询教材
     */
    List<Textbook> selectByCondition(@Param("title") String title, 
                                   @Param("author") String author,
                                   @Param("categoryId") Integer categoryId);
    
    /**
     * 添加教材
     */
    int insert(Textbook textbook);
    
    /**
     * 更新教材
     */
    int update(Textbook textbook);
    
    /**
     * 删除教材
     */
    int deleteById(Integer id);
    
    /**
     * 更新库存
     */
    int updateStock(@Param("id") Integer id, @Param("stock") Integer stock);
    
    /**
     * 统计教材总数
     */
    int count();
    
    /**
     * 分页查询
     */
    List<Textbook> selectByPage(@Param("offset") int offset, @Param("limit") int limit);
} 