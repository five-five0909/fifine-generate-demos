package com.example.entity;

import java.util.Date;

/**
 * 借阅记录实体类
 */
public class BorrowRecord {
    private Integer id;
    private Integer textbookId;
    private String textbookTitle;
    private String studentName;
    private String studentNumber;
    private String phone;
    private Date borrowDate;
    private Date returnDate;
    private Date actualReturnDate;
    private Integer quantity;
    private String status; // BORROWED, RETURNED, OVERDUE
    private String remark;
    private Date createTime;
    private Date updateTime;

    public BorrowRecord() {}

    public BorrowRecord(Integer textbookId, String studentName, String studentNumber, 
                       String phone, Date borrowDate, Date returnDate, Integer quantity, String remark) {
        this.textbookId = textbookId;
        this.studentName = studentName;
        this.studentNumber = studentNumber;
        this.phone = phone;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.quantity = quantity;
        this.status = "BORROWED";
        this.remark = remark;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTextbookId() {
        return textbookId;
    }

    public void setTextbookId(Integer textbookId) {
        this.textbookId = textbookId;
    }

    public String getTextbookTitle() {
        return textbookTitle;
    }

    public void setTextbookTitle(String textbookTitle) {
        this.textbookTitle = textbookTitle;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(Date actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BorrowRecord{" +
                "id=" + id +
                ", textbookId=" + textbookId +
                ", textbookTitle='" + textbookTitle + '\'' +
                ", studentName='" + studentName + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                ", phone='" + phone + '\'' +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                ", actualReturnDate=" + actualReturnDate +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 