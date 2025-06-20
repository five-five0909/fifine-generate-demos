package com.qst.itoffer.bean;

import java.util.ArrayList;
import java.util.List;

import com.qst.itoffer.dao.UserDAO;
/**
 * 用户列表分页Bean，包含每页大小、当前页码、总页数、数据列表及翻页标志。
 */
public class UserPageBean {
	// 每页记录数
	private int pageSize = 10;
	// 当前页码
	private int pageNo = 1;
	// 总页数
	private int totalPages;
	// 当前页数据列表
	private List<User> pageData = new ArrayList<User>();
	// 是否有下一页
	private boolean hasNextPage;
	// 是否有上一页
	private boolean hasPreviousPage;

	/**
	 * 获取每页记录数。
	 */
	public int getPageSize() { return pageSize; }
	/**
	 * 设置每页记录数。
	 */
	public void setPageSize(int pageSize) { this.pageSize = pageSize; }
	/**
	 * 获取当前页码。
	 */
	public int getPageNo() { return pageNo; }
	/**
	 * 设置当前页码。
	 */
	public void setPageNo(int pageNo) { this.pageNo = pageNo; }
	/**
	 * 设置总页数。
	 */
	public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
	/**
	 * 设置当前页数据列表。
	 */
	public void setPageData(List<User> pageData) { this.pageData = pageData; }
	/**
	 * 设置是否有下一页。
	 */
	public void setHasNextPage(boolean hasNextPage) { this.hasNextPage = hasNextPage; }
	/**
	 * 是否有上一页。
	 */
	public boolean isHasPreviousPage() { return hasPreviousPage; }
	/**
	 * 设置是否有上一页。
	 */
	public void setHasPreviousPage(boolean hasPreviousPage) { this.hasPreviousPage = hasPreviousPage; }
	/**
	 * 全字段构造方法。
	 */
	public UserPageBean(int pageSize, int pageNo, int totalPages, List<User> pageData, boolean hasNextPage, boolean hasPreviousPage) {
		this.pageSize = pageSize;
		this.pageNo = pageNo;
		this.totalPages = totalPages;
		this.pageData = pageData;
		this.hasNextPage = hasNextPage;
		this.hasPreviousPage = hasPreviousPage;
	}
	/**
	 * 默认构造方法。
	 */
	public UserPageBean() {}
	/**
	 * 获取总页数（根据记录数计算）。
	 */
	public int getTotalPages() {
		UserDAO dao = new UserDAO();
		int recordCount = dao.getRecordCount();
		return (recordCount + pageSize - 1) / pageSize;
	}
	/**
	 * 获取当前页数据列表（从DAO获取）。
	 */
	public List<User> getPageData() {
		UserDAO dao = new UserDAO();
		List<User> list = dao.getUserPageList(pageNo, pageSize);
		return list;
	}
	/**
	 * 是否有下一页（计算得出）。
	 */
	public boolean isHasNextPage() {
		return (this.getPageNo() < this.getTotalPages());
	}
}