package com.qst.itoffer.bean;

import java.sql.Timestamp;
import java.util.Date;
/**
 * 职位实体类，包含职位详情、所属公司和招聘信息。
 */
public class Job {
	// 职位ID
	private int jobId;
	// 公司实体
	private Company company;
	// 职位名称
	private String jobTitle;
	// 招聘人数
	private int jobCount;
	// 薪资
	private String jobSalary;
	// 工作地点
	private String jobPlace;
	// 职位描述
	private String jobDesc;
	// 截止日期
	private Date jobEndDate;
	// 职位状态（1: 招聘中，2: 暂停，3: 关闭）
	private int jobState;
	// 申请人数
	private int applyCount;

	/**
	 * 默认构造方法。
	 */
	public Job() {}

	/**
	 * 全字段构造方法。
	 */
	public Job(int jobId, Company company, String jobTitle, int jobCount, String jobSalary, String jobPlace, String jobDesc, Date jobEndDate, int jobState, int applyCount) {
		this.jobId = jobId;
		this.company = company;
		this.jobTitle = jobTitle;
		this.jobCount = jobCount;
		this.jobSalary = jobSalary;
		this.jobPlace = jobPlace;
		this.jobDesc = jobDesc;
		this.jobEndDate = jobEndDate;
		this.jobState = jobState;
		this.applyCount = applyCount;
	}

	/**
	 * 获取职位ID。
	 */
	public int getJobId() { return jobId; }
	/**
	 * 设置职位ID。
	 */
	public void setJobId(int jobId) { this.jobId = jobId; }
	/**
	 * 获取公司实体。
	 */
	public Company getCompany() { return company; }
	/**
	 * 设置公司实体。
	 */
	public void setCompany(Company company) { this.company = company; }
	/**
	 * 获取职位名称。
	 */
	public String getJobTitle() { return jobTitle; }
	/**
	 * 设置职位名称。
	 */
	public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
	/**
	 * 获取招聘人数。
	 */
	public int getJobCount() { return jobCount; }
	/**
	 * 设置招聘人数。
	 */
	public void setJobCount(int jobCount) { this.jobCount = jobCount; }
	/**
	 * 获取薪资。
	 */
	public String getJobSalary() { return jobSalary; }
	/**
	 * 设置薪资。
	 */
	public void setJobSalary(String jobSalary) { this.jobSalary = jobSalary; }
	/**
	 * 获取工作地点。
	 */
	public String getJobPlace() { return jobPlace; }
	/**
	 * 设置工作地点。
	 */
	public void setJobPlace(String jobPlace) { this.jobPlace = jobPlace; }
	/**
	 * 获取职位描述。
	 */
	public String getJobDesc() { return jobDesc; }
	/**
	 * 设置职位描述。
	 */
	public void setJobDesc(String jobDesc) { this.jobDesc = jobDesc; }
	/**
	 * 获取截止日期。
	 */
	public Date getJobEndDate() { return jobEndDate; }
	/**
	 * 设置截止日期。
	 */
	public void setJobEndDate(Date jobEndDate) { this.jobEndDate = jobEndDate; }
	/**
	 * 获取职位状态。
	 */
	public int getJobState() { return jobState; }
	/**
	 * 设置职位状态。
	 */
	public void setJobState(int jobState) { this.jobState = jobState; }
	/**
	 * 获取申请人数。
	 */
	public int getApplyCount() { return applyCount; }
	/**
	 * 设置申请人数。
	 */
	public void setApplyCount(int applyCount) { this.applyCount = applyCount; }
}
