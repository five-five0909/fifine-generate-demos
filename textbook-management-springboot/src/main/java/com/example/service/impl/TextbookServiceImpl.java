package com.example.service.impl;

import com.example.dao.TextbookDao;
import com.example.entity.Textbook;
import com.example.service.TextbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 教材服务实现类
 */
@Service
@Transactional
public class TextbookServiceImpl implements TextbookService {

    @Autowired
    private TextbookDao textbookDao;

    @Override
    public List<Textbook> getAllTextbooks() {
        return textbookDao.selectAll();
    }

    @Override
    public Textbook getTextbookById(Integer id) {
        return textbookDao.selectById(id);
    }

    @Override
    public List<Textbook> getTextbooksByCondition(String title, String author, Integer categoryId) {
        return textbookDao.selectByCondition(title, author, categoryId);
    }

    @Override
    public boolean addTextbook(Textbook textbook) {
        return textbookDao.insert(textbook) > 0;
    }

    @Override
    public boolean updateTextbook(Textbook textbook) {
        return textbookDao.update(textbook) > 0;
    }

    @Override
    public boolean deleteTextbook(Integer id) {
        return textbookDao.deleteById(id) > 0;
    }

    @Override
    public boolean updateStock(Integer id, Integer stock) {
        return textbookDao.updateStock(id, stock) > 0;
    }

    @Override
    public int getTextbookCount() {
        return textbookDao.count();
    }

    @Override
    public List<Textbook> getTextbooksByPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return textbookDao.selectByPage(offset, pageSize);
    }
} 