package com.qst.itoffer.bean;

import java.util.ArrayList;
import java.util.List;
/**
 * 通用分页Bean，处理分页数据。
 * 包含每页大小、当前页码、总页数、总记录数和数据列表。
 */
public class PageBean<T> {
    // 每页记录数
    private int pageSize = 10;
    // 当前页码
    private int pageNo = 1;
    // 总页数
    private int totalPages;
    // 总记录数
    private int recordCount;
    // 当前页数据列表
    private List<T> pageData = new ArrayList<T>();
    // 是否有下一页
    private boolean hasNextPage;
    // 是否有上一页
    private boolean hasPreviousPage;

    /**
     * 默认构造方法。
     */
    public PageBean() {
    }
    /**
     * 带页大小和页码的构造方法。
     */
    public PageBean(int pageSize, int pageNo) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }
    /**
     * 获取总页数。
     */
    public int getTotalPages() {
        return (recordCount + pageSize - 1) / pageSize;
    }
    /**
     * 获取当前页数据列表。
     */
    public List<T> getPageData() {
        return pageData;
    }
    /**
     * 设置当前页数据列表。
     */
    public void setPageData(List<T> pageData) {
        this.pageData = pageData;
    }
    /**
     * 是否有下一页。
     */
    public boolean isHasNextPage() {
        return (this.getPageNo() < this.getTotalPages());
    }
    /**
     * 设置是否有下一页。
     */
    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
    /**
     * 是否有上一页。
     */
    public boolean isHasPreviousPage() {
        return (this.getPageNo() > 1);
    }
    /**
     * 设置是否有上一页。
     */
    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }
    /**
     * 获取每页记录数。
     */
    public int getPageSize() {
        return pageSize;
    }
    /**
     * 设置每页记录数。
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    /**
     * 获取当前页码。
     */
    public int getPageNo() {
        return pageNo;
    }
    /**
     * 设置当前页码。
     */
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
    /**
     * 获取总记录数。
     */
    public int getRecordCount() {
        return recordCount;
    }
    /**
     * 设置总记录数。
     */
    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }
    /**
     * 设置总页数。
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}