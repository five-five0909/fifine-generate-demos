package com.huawei.itoffer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.huawei.itoffer.bean.Company;
import com.huawei.itoffer.bean.Job;
import com.huawei.itoffer.util.DBUtil;

public class JobDAO {
	Connection conn = DBUtil.getConnection();

	public List<Job> getJobListByCompanyID(String companyID) {
		List<Job> list = new ArrayList<Job>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM tb_job WHERE company_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(companyID));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Job job = new Job();
				job.setJobId(rs.getInt("job_id"));
				job.setJobName(rs.getString("job_name"));
				job.setJobSalary(rs.getString("job_salary"));
				job.setJobArea(rs.getString("job_area"));
				job.setJobEnddate(rs.getTimestamp("job_endtime"));
				job.setJobHiringnum(rs.getInt("job_hiringnum"));
				list.add(job);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public Job getJobByID(String jobid) {
		Job job = new Job();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT tb_job.*,company_pic " + "FROM tb_job,tb_company "
					+ "where tb_job.company_id =  tb_company.company_id " + "and job_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(jobid));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				job.setJobId(rs.getInt("job_id"));
				job.setJobName(rs.getString("job_name"));
				job.setJobHiringnum(rs.getInt("job_hiringnum"));
				job.setJobSalary(rs.getString("job_salary"));
				job.setJobArea(rs.getString("job_area"));
				job.setJobDesc(rs.getString("job_desc"));
				job.setJobEnddate(rs.getTimestamp("job_endtime"));
				job.setJobState(rs.getInt("job_state"));
				Company company = new Company();
				company.setCompanyId(rs.getInt("company_id"));
				company.setCompanyPic(rs.getString("company_pic"));
				job.setCompany(company);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeJDBC(rs, pstmt, conn);
		}
		return job;
	}

	public boolean deleteJob(String applyId) {
		boolean result = false;
		String sql = "delete from tb_jobapply where apply_id=?";
		try {
			PreparedStatement ps = this.conn.prepareStatement(sql);
			ps.setString(1, applyId);
			int i = ps.executeUpdate();
			if (i > 0)
				result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
