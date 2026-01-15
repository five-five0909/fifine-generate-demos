package com.example.dao;

import com.example.entity.BorrowRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 借阅记录数据访问接口
 */
public interface BorrowRecordDao {
    
    /**
     * 查询所有借阅记录
     */
    List<BorrowRecord> selectAll();
    
    /**
     * 根据ID查询借阅记录
     */
    BorrowRecord selectById(Integer id);
    
    /**
     * 根据条件查询借阅记录
     */
    List<BorrowRecord> selectByCondition(@Param("studentName") String studentName,
                                       @Param("studentNumber") String studentNumber,
                                       @Param("status") String status);
    
    /**
     * 添加借阅记录
     */
    int insert(BorrowRecord borrowRecord);
    
    /**
     * 更新借阅记录
     */
    int update(BorrowRecord borrowRecord);
    
    /**
     * 删除借阅记录
     */
    int deleteById(Integer id);
    
    /**
     * 根据学生学号查询借阅记录
     */
    List<BorrowRecord> selectByStudentNumber(String studentNumber);
    
    /**
     * 查询逾期记录
     */
    List<BorrowRecord> selectOverdueRecords();
    
    /**
     * 统计借阅记录总数
     */
    int count();
    
    /**
     * 分页查询
     */
    List<BorrowRecord> selectByPage(@Param("offset") int offset, @Param("limit") int limit);
} 