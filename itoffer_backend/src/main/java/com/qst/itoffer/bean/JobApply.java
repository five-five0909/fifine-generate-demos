package com.qst.itoffer.bean;

import java.util.Date;
/**
 * 职位申请实体类，包含申请详情、申请人和职位信息。
 */
public class JobApply {
	// 申请ID
	private int applyId;
	// 职位ID
	private int jobId;
	// 申请人实体
	private Applicant applicant;
	// 申请日期
	private Date applyDate;
	// 申请状态
	private int applyState;
	// 职位实体
	private Job job;

	/**
	 * 获取申请ID。
	 */
	public int getApplyId() { return applyId; }
	/**
	 * 设置申请ID。
	 */
	public void setApplyId(int applyId) { this.applyId = applyId; }
	/**
	 * 获取职位ID。
	 */
	public int getJobId() { return jobId; }
	/**
	 * 设置职位ID。
	 */
	public void setJobId(int jobId) { this.jobId = jobId; }
	/**
	 * 获取申请人实体。
	 */
	public Applicant getApplicant() { return applicant; }
	/**
	 * 设置申请人实体。
	 */
	public void setApplicant(Applicant applicant) { this.applicant = applicant; }
	/**
	 * 获取申请日期。
	 */
	public Date getApplyDate() { return applyDate; }
	/**
	 * 设置申请日期。
	 */
	public void setApplyDate(Date applyDate) { this.applyDate = applyDate; }
	/**
	 * 获取申请状态。
	 */
	public int getApplyState() { return applyState; }
	/**
	 * 设置申请状态。
	 */
	public void setApplyState(int applyState) { this.applyState = applyState; }
	/**
	 * 获取职位实体。
	 */
	public Job getJob() { return job; }
	/**
	 * 设置职位实体。
	 */
	public void setJob(Job job) { this.job = job; }
}
