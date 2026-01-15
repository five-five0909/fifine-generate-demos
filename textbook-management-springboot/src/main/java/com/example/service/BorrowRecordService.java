package com.example.service;

import com.example.entity.BorrowRecord;

import java.util.List;

/**
 * 借阅记录服务接口
 */
public interface BorrowRecordService {
    
    /**
     * 查询所有借阅记录
     */
    List<BorrowRecord> getAllBorrowRecords();
    
    /**
     * 根据ID查询借阅记录
     */
    BorrowRecord getBorrowRecordById(Integer id);
    
    /**
     * 根据条件查询借阅记录
     */
    List<BorrowRecord> getBorrowRecordsByCondition(String studentName, String studentNumber, String status);
    
    /**
     * 添加借阅记录
     */
    boolean addBorrowRecord(BorrowRecord borrowRecord);
    
    /**
     * 更新借阅记录
     */
    boolean updateBorrowRecord(BorrowRecord borrowRecord);
    
    /**
     * 删除借阅记录
     */
    boolean deleteBorrowRecord(Integer id);
    
    /**
     * 根据学生学号查询借阅记录
     */
    List<BorrowRecord> getBorrowRecordsByStudentNumber(String studentNumber);
    
    /**
     * 查询逾期记录
     */
    List<BorrowRecord> getOverdueRecords();
    
    /**
     * 统计借阅记录总数
     */
    int getBorrowRecordCount();
    
    /**
     * 分页查询
     */
    List<BorrowRecord> getBorrowRecordsByPage(int pageNum, int pageSize);
    
    /**
     * 归还教材
     */
    boolean returnTextbook(Integer borrowRecordId);
    
    /**
     * 借阅教材
     */
    boolean borrowTextbook(BorrowRecord borrowRecord);
} 