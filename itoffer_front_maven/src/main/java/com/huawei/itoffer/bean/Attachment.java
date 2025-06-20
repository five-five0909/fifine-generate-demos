package com.huawei.itoffer.bean;

public class Attachment {
    private int attachmentId;
    private int applicantId;
    private String fileName;
    private String filePath;

    public int getAttachmentId() { return attachmentId; }
    public void setAttachmentId(int attachmentId) { this.attachmentId = attachmentId; }
    public int getApplicantId() { return applicantId; }
    public void setApplicantId(int applicantId) { this.applicantId = applicantId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
} 