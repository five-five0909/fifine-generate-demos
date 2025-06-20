package com.qst.itoffer.bean;

import java.sql.Timestamp;

/**
 * Job entity class representing job details, company information, and recruitment data.
 */
public class Job {
	// Job ID
	private int jobId;
	// Company entity
	private Company company;
	// Job name
	private String jobName;
	// Number of positions to hire
	private int jobHiringnum;
	// Salary information
	private String jobSalary;
	// Job location
	private String jobPlace;
	// Job description
	private String jobDesc;
	// End date for job application
	private Timestamp jobEndtime;
	// Job state (1: Open, 2: Paused, 3: Closed)
	private int jobState;
	// Number of applicants
	private int applyNum;

	/**
	 * Default constructor.
	 */
	public Job() {}

	/**
	 * Full-argument constructor.
	 */
	public Job(int jobId, Company company, String jobName, int jobHiringnum, String jobSalary, String jobPlace, String jobDesc, Timestamp jobEndtime, int jobState, int applyNum) {
		this.jobId = jobId;
		this.company = company;
		this.jobName = jobName;
		this.jobHiringnum = jobHiringnum;
		this.jobSalary = jobSalary;
		this.jobPlace = jobPlace;
		this.jobDesc = jobDesc;
		this.jobEndtime = jobEndtime;
		this.jobState = jobState;
		this.applyNum = applyNum;
	}

	/**
	 * Gets the job ID.
	 */
	public int getJobId() { return jobId; }
	/**
	 * Sets the job ID.
	 */
	public void setJobId(int jobId) { this.jobId = jobId; }

	/**
	 * Gets the company entity.
	 */
	public Company getCompany() { return company; }
	/**
	 * Sets the company entity.
	 */
	public void setCompany(Company company) { this.company = company; }

	/**
	 * Gets the job name.
	 */
	public String getJobName() { return jobName; }
	/**
	 * Sets the job name.
	 */
	public void setJobName(String jobName) { this.jobName = jobName; }

	/**
	 * Gets the number of positions to hire.
	 */
	public int getJobHiringnum() { return jobHiringnum; }
	/**
	 * Sets the number of positions to hire.
	 */
	public void setJobHiringnum(int jobHiringnum) { this.jobHiringnum = jobHiringnum; }

	/**
	 * Gets the salary information.
	 */
	public String getJobSalary() { return jobSalary; }
	/**
	 * Sets the salary information.
	 */
	public void setJobSalary(String jobSalary) { this.jobSalary = jobSalary; }

	/**
	 * Gets the job location.
	 */
	public String getJobPlace() { return jobPlace; }
	/**
	 * Sets the job location.
	 */
	public void setJobPlace(String jobPlace) { this.jobPlace = jobPlace; }

	/**
	 * Gets the job description.
	 */
	public String getJobDesc() { return jobDesc; }
	/**
	 * Sets the job description.
	 */
	public void setJobDesc(String jobDesc) { this.jobDesc = jobDesc; }

	/**
	 * Gets the end date for job application.
	 */
	public Timestamp getJobEndtime() { return jobEndtime; }
	/**
	 * Sets the end date for job application.
	 */
	public void setJobEndtime(Timestamp jobEndtime) { this.jobEndtime = jobEndtime; }

	/**
	 * Gets the job state.
	 */
	public int getJobState() { return jobState; }
	/**
	 * Sets the job state.
	 */
	public void setJobState(int jobState) { this.jobState = jobState; }

	/**
	 * Gets the number of applicants.
	 */
	public int getApplyNum() { return applyNum; }
	/**
	 * Sets the number of applicants.
	 */
	public void setApplyNum(int applyNum) { this.applyNum = applyNum; }
}
