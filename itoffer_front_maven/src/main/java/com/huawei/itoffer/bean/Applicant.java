package com.huawei.itoffer.bean;

import java.util.Date;

public class Applicant {
	
		private int applicantId;
		
		private String applicantEmail;
		
		private String applicantPwd;
		
		private Date applicantRegistDate;

		public int getApplicantId() {
			return applicantId;
		}

		public void setApplicantId(int applicantId) {
			this.applicantId = applicantId;
		}

		public String getApplicantEmail() {
			return applicantEmail;
		}

		public void setApplicantEmail(String applicantEmail) {
			this.applicantEmail = applicantEmail;
		}

		public String getApplicantPwd() {
			return applicantPwd;
		}

		public void setApplicantPwd(String applicantPwd) {
			this.applicantPwd = applicantPwd;
		}

		public Date getApplicantRegistDate() {
			return applicantRegistDate;
		}

		public void setApplicantRegistDate(Date applicantRegistDate) {
			this.applicantRegistDate = applicantRegistDate;
		}
		
		
		
		
}
