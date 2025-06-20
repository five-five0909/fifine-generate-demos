package com.example.service.impl;

import com.example.dao.BorrowRecordDao;
import com.example.dao.TextbookDao;
import com.example.entity.BorrowRecord;
import com.example.entity.Textbook;
import com.example.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 借阅记录服务实现类
 */
@Service
@Transactional
public class BorrowRecordServiceImpl implements BorrowRecordService {

    @Autowired
    private BorrowRecordDao borrowRecordDao;

    @Autowired
    private TextbookDao textbookDao;

    @Override
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordDao.selectAll();
    }

    @Override
    public BorrowRecord getBorrowRecordById(Integer id) {
        return borrowRecordDao.selectById(id);
    }

    @Override
    public List<BorrowRecord> getBorrowRecordsByCondition(String studentName, String studentNumber, String status) {
        return borrowRecordDao.selectByCondition(studentName, studentNumber, status);
    }

    @Override
    public boolean addBorrowRecord(BorrowRecord borrowRecord) {
        return borrowRecordDao.insert(borrowRecord) > 0;
    }

    @Override
    public boolean updateBorrowRecord(BorrowRecord borrowRecord) {
        return borrowRecordDao.update(borrowRecord) > 0;
    }

    @Override
    public boolean deleteBorrowRecord(Integer id) {
        return borrowRecordDao.deleteById(id) > 0;
    }

    @Override
    public List<BorrowRecord> getBorrowRecordsByStudentNumber(String studentNumber) {
        return borrowRecordDao.selectByStudentNumber(studentNumber);
    }

    @Override
    public List<BorrowRecord> getOverdueRecords() {
        return borrowRecordDao.selectOverdueRecords();
    }

    @Override
    public int getBorrowRecordCount() {
        return borrowRecordDao.count();
    }

    @Override
    public List<BorrowRecord> getBorrowRecordsByPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return borrowRecordDao.selectByPage(offset, pageSize);
    }

    @Override
    @Transactional
    public boolean returnTextbook(Integer borrowRecordId) {
        try {
            // 获取借阅记录
            BorrowRecord borrowRecord = borrowRecordDao.selectById(borrowRecordId);
            if (borrowRecord == null || !"BORROWED".equals(borrowRecord.getStatus())) {
                return false;
            }

            // 更新借阅记录状态
            borrowRecord.setActualReturnDate(new Date());
            borrowRecord.setStatus("RETURNED");
            borrowRecordDao.update(borrowRecord);

            // 增加教材库存
            Textbook textbook = textbookDao.selectById(borrowRecord.getTextbookId());
            if (textbook != null) {
                int newStock = textbook.getStock() + borrowRecord.getQuantity();
                textbookDao.updateStock(textbook.getId(), newStock);
            }

            return true;
        } catch (Exception e) {
            throw new RuntimeException("归还教材失败", e);
        }
    }

    @Override
    @Transactional
    public boolean borrowTextbook(BorrowRecord borrowRecord) {
        try {
            // 检查库存
            Textbook textbook = textbookDao.selectById(borrowRecord.getTextbookId());
            if (textbook == null) {
                throw new RuntimeException("教材不存在");
            }
            
            if (textbook.getStock() < borrowRecord.getQuantity()) {
                throw new RuntimeException("库存不足，当前库存：" + textbook.getStock());
            }

            // 设置借阅状态
            borrowRecord.setStatus("BORROWED");
            
            // 添加借阅记录
            int result = borrowRecordDao.insert(borrowRecord);
            if (result <= 0) {
                return false;
            }

            // 减少库存
            int newStock = textbook.getStock() - borrowRecord.getQuantity();
            textbookDao.updateStock(textbook.getId(), newStock);

            return true;
        } catch (Exception e) {
            throw new RuntimeException("借阅教材失败：" + e.getMessage(), e);
        }
    }
} 