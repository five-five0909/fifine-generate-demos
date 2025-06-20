package com.huawei.itoffer.bean;

import java.util.Date;

public class Project {
    private int projectId;
    private int applicantId;
    private String projectName;
    private Date startDate;
    private Date endDate;
    private String position;

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }
    public int getApplicantId() { return applicantId; }
    public void setApplicantId(int applicantId) { this.applicantId = applicantId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
} 