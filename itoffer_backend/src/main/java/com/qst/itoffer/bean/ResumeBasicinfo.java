package com.qst.itoffer.bean;
import java.util.Date;
/**
 * 简历基本信息实体类，包含个人信息、联系方式和求职意向。
 */
public class ResumeBasicinfo {
	// 简历ID
	private int basicinfoId;
	// 真实姓名
	private String realName;
	// 性别
	private String gender;
	// 出生日期
	private Date birthday;
	// 现居地
	private String currentLoc;
	// 户口所在地
	private String residentLoc;
	// 电话
	private String telephone;
	// 邮箱
	private String email;
	// 求职意向
	private String jobIntension;
	// 工作经验
	private String jobExperience;
	// 头像
	private String headShot;

	/**
	 * 默认构造方法。
	 */
	public ResumeBasicinfo() {
	}

	/**
	 * 除ID和头像外的全字段构造方法。
	 */
	public ResumeBasicinfo(String realName, String gender, Date birthday,
						   String currentLoc, String residentLoc, String telephone,
						   String email, String jobIntension, String jobExperience) {
		super();
		this.realName = realName;
		this.gender = gender;
		this.birthday = birthday;
		this.currentLoc = currentLoc;
		this.residentLoc = residentLoc;
		this.telephone = telephone;
		this.email = email;
		this.jobIntension = jobIntension;
		this.jobExperience = jobExperience;
	}

	/**
	 * 获取简历ID。
	 */
	public int getBasicinfoId() {
		return basicinfoId;
	}

	/**
	 * 设置简历ID。
	 */
	public void setBasicinfoId(int basicinfoId) {
		this.basicinfoId = basicinfoId;
	}

	/**
	 * 获取真实姓名。
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置真实姓名。
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * 获取性别。
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * 设置性别。
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * 获取出生日期。
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * 设置出生日期。
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * 获取现居地。
	 */
	public String getCurrentLoc() {
		return currentLoc;
	}

	/**
	 * 设置现居地。
	 */
	public void setCurrentLoc(String currentLoc) {
		this.currentLoc = currentLoc;
	}

	/**
	 * 获取户口所在地。
	 */
	public String getResidentLoc() {
		return residentLoc;
	}

	/**
	 * 设置户口所在地。
	 */
	public void setResidentLoc(String residentLoc) {
		this.residentLoc = residentLoc;
	}

	/**
	 * 获取电话。
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * 设置电话。
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * 获取邮箱。
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置邮箱。
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取求职意向。
	 */
	public String getJobIntension() {
		return jobIntension;
	}

	/**
	 * 设置求职意向。
	 */
	public void setJobIntension(String jobIntension) {
		this.jobIntension = jobIntension;
	}

	/**
	 * 获取工作经验。
	 */
	public String getJobExperience() {
		return jobExperience;
	}

	/**
	 * 设置工作经验。
	 */
	public void setJobExperience(String jobExperience) {
		this.jobExperience = jobExperience;
	}

	/**
	 * 获取头像。
	 */
	public String getHeadShot() {
		return headShot;
	}

	/**
	 * 设置头像。
	 */
	public void setHeadShot(String headShot) {
		this.headShot = headShot;
	}

	//......省略setter和getter方法
}