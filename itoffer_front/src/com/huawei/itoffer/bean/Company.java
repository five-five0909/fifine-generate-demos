package com.huawei.itoffer.bean;

import java.util.HashSet;
import java.util.Set;

public class Company {
	
		private int companyId;
	
		private String companyName;
		
		private String companyArea;
		
		private String companySize;
		
		private String companyType;
		
		private String companyBrief;
		
		private int companyState;
	
		private int comanySort;
		
		private int companyViewnum;
		
		private String companyPic;
		
		private Set<Job> jobs = new HashSet<Job>();
		
		public int getCompanyId() {
			return companyId;
		}
		public void setCompanyId(int companyId) {
			this.companyId = companyId;
		}
	
		public String getCompanyName() {
			return companyName;
		}
		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}
		public String getCompanyArea() {
			return companyArea;
		}
		public void setCompanyArea(String companyArea) {
			this.companyArea = companyArea;
		}
		public String getCompanySize() {
			return companySize;
		}
		public void setCompanySize(String companySize) {
			this.companySize = companySize;
		}
		public String getCompanyType() {
			return companyType;
		}
		public void setCompanyType(String companyType) {
			this.companyType = companyType;
		}
		public String getCompanyBrief() {
			return companyBrief;
		}
		public void setCompanyBrief(String companyBrief) {
			this.companyBrief = companyBrief;
		}
		public int getCompanyState() {
			return companyState;
		}
		public void setCompanyState(int companyState) {
			this.companyState = companyState;
		}
		public int getComanySort() {
			return comanySort;
		}
		public void setComanySort(int comanySort) {
			this.comanySort = comanySort;
		}
		public int getCompanyViewnum() {
			return companyViewnum;
		}
		public void setCompanyViewnum(int companyViewnum) {
			this.companyViewnum = companyViewnum;
		}
		public String getCompanyPic() {
			return companyPic;
		}
		public void setCompanyPic(String companyPic) {
			this.companyPic = companyPic;
		}
		public Set<Job> getJobs() {
			return jobs;
		}
		public void setJobs(Set<Job> jobs) {
			this.jobs = jobs;
		}
		
		
}
