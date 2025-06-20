package com.huawei.itoffer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.huawei.itoffer.bean.Company;
import com.huawei.itoffer.bean.Job;
import com.huawei.itoffer.util.DBUtil;

public class CompanyDAO {
	public List<Company> getCompanyList() {
        List<Company> list = new ArrayList<Company>();
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
        String sql = 
        "SELECT tb_company.company_id,company_pic,company_name,job_id,job_name, job_salary,job_area,job_endtime "
        + "FROM tb_company "
        + "LEFT OUTER JOIN tb_job ON tb_job.company_id=tb_company.company_id "
        + "WHERE company_state=1 and job_id IN ("
        + "SELECT MAX(job_id) FROM tb_job WHERE job_state=1 GROUP BY company_id"
        + ")";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Company company = new Company();
                Job job = new Job();
                company.setCompanyId(rs.getInt("company_id"));
                company.setCompanyName(rs.getString("company_name"));
                company.setCompanyPic(rs.getString("company_pic"));
                job.setJobId(rs.getInt("job_id"));
                job.setJobName(rs.getString("job_name"));
                job.setJobSalary(rs.getString("job_salary"));
                job.setJobArea(rs.getString("job_area"));
                job.setJobEnddate(rs.getTimestamp("job_endtime"));
                company.getJobs().add(job);
                list.add(company);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

	public Company getCompanyByID(String companyID) {
		Company company = null;
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM tb_company WHERE company_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(companyID));
            rs = pstmt.executeQuery();
            if (rs.next()) {
            	company = new Company();
                company.setCompanyId(rs.getInt("company_id"));
                company.setCompanyArea(rs.getString("company_area"));
                company.setCompanyBrief(rs.getString("company_brief"));
                company.setCompanyPic(rs.getString("company_pic"));
                company.setCompanySize(rs.getString("company_size"));
                company.setCompanyType(rs.getString("company_type"));
                company.setCompanyViewnum(rs.getInt("company_viewnum"));
                company.setCompanyName(rs.getString("company_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return company;
	}

	public void updateCompanyViewCount(int id) {
		Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "UPDATE tb_company "
                    + "SET company_viewnum=company_viewnum+1 "
                    + "WHERE company_id=? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeJDBC(rs, pstmt, conn);
        }
	}
}
