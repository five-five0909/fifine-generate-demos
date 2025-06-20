package com.huawei.itoffer.bean;

import java.util.Date;

public class Education {
    private int educationId;
    private int applicantId;
    private String school;
    private Date startDate;
    private Date endDate;
    private String degree;
    private String major;

    public int getEducationId() { return educationId; }
    public void setEducationId(int educationId) { this.educationId = educationId; }
    public int getApplicantId() { return applicantId; }
    public void setApplicantId(int applicantId) { this.applicantId = applicantId; }
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
} 