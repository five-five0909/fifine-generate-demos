package com.qst.itoffer.bean;

import java.util.Date;

/**
 * 求职者实体类，包含账号、注册时间和简历信息。
 */
public class Applicant {
	// 求职者ID
	private int applicantId;
	// 求职者邮箱
	private String applicantEmail;
	// 求职者密码
	private String applicantPwd;
	// 注册时间
	private Date applicantRegistDate;
	// 简历基本信息实体
	private ResumeBasicinfo resume;

	/**
	 * 默认构造方法。
	 */
	public Applicant() {
		super();
	}

	/**
	 * 带ID、邮箱和密码的构造方法。
	 */
	public Applicant(int applicantId, String applicantEmail, String applicantPwd) {
		super();
		this.applicantId = applicantId;
		this.applicantEmail = applicantEmail;
		this.applicantPwd = applicantPwd;
	}

	/**
	 * 获取求职者ID。
	 */
	public int getApplicantId() {
		return applicantId;
	}

	/**
	 * 设置求职者ID。
	 */
	public void setApplicantId(int applicantId) {
		this.applicantId = applicantId;
	}

	/**
	 * 获取求职者邮箱。
	 */
	public String getApplicantEmail() {
		return applicantEmail;
	}

	/**
	 * 设置求职者邮箱。
	 */
	public void setApplicantEmail(String applicantEmail) {
		this.applicantEmail = applicantEmail;
	}

	/**
	 * 获取求职者密码。
	 */
	public String getApplicantPwd() {
		return applicantPwd;
	}

	/**
	 * 设置求职者密码。
	 */
	public void setApplicantPwd(String applicantPwd) {
		this.applicantPwd = applicantPwd;
	}

	/**
	 * 获取注册时间。
	 */
	public Date getApplicantRegistDate() {
		return applicantRegistDate;
	}

	/**
	 * 设置注册时间。
	 */
	public void setApplicantRegistDate(Date applicantRegistDate) {
		this.applicantRegistDate = applicantRegistDate;
	}

	/**
	 * 获取简历基本信息实体。
	 */
	public ResumeBasicinfo getResume() {
		return resume;
	}

	/**
	 * 设置简历基本信息实体。
	 */
	public void setResume(ResumeBasicinfo resume) {
		this.resume = resume;
	}

}
