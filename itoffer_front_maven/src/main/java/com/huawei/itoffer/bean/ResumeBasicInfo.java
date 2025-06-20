package com.huawei.itoffer.bean;

import java.util.Date;

public class ResumeBasicInfo {
    private int basicinfoId;
    private int applicantId;
    private String realname;
    private String gender;
    private Date birthday;
    private String currentLoc;
    private String residentLoc;
    private String telephone;
    private String email;
    private String jobIntension;
    private String jobExperience;
    private String headShot;

    public int getBasicinfoId() { return basicinfoId; }
    public void setBasicinfoId(int basicinfoId) { this.basicinfoId = basicinfoId; }
    public int getApplicantId() { return applicantId; }
    public void setApplicantId(int applicantId) { this.applicantId = applicantId; }
    public String getRealname() { return realname; }
    public void setRealname(String realname) { this.realname = realname; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Date getBirthday() { return birthday; }
    public void setBirthday(Date birthday) { this.birthday = birthday; }
    public String getCurrentLoc() { return currentLoc; }
    public void setCurrentLoc(String currentLoc) { this.currentLoc = currentLoc; }
    public String getResidentLoc() { return residentLoc; }
    public void setResidentLoc(String residentLoc) { this.residentLoc = residentLoc; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getJobIntension() { return jobIntension; }
    public void setJobIntension(String jobIntension) { this.jobIntension = jobIntension; }
    public String getJobExperience() { return jobExperience; }
    public void setJobExperience(String jobExperience) { this.jobExperience = jobExperience; }
    public String getHeadShot() { return headShot; }
    public void setHeadShot(String headShot) { this.headShot = headShot; }
} 