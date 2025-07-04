package com.qst.itoffer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.qst.itoffer.bean.Company;
import com.qst.itoffer.bean.Job;
import com.qst.itoffer.util.DBUtil;
/**
 * 职位信息数据库操作类
 */
public class JobDAO {
    /**
     * 查询所有职位信息以及每个职位的申请人数
     * @return
     */
    public List<Job> selectAll() {
        List<Job> list = new ArrayList<Job>();
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        try {
            String sql = "SELECT tb_job.*,company_name "
                    + "FROM tb_job "
                    + "INNER JOIN tb_company on tb_job.company_id =  tb_company.company_id ";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Job job = new Job();
                // 查询某个职位的申请人数
                String sql2 = "SELECT COUNT(*) FROM tb_jobapply WHERE job_id = ?";
                pstmt2 = conn.prepareStatement(sql2);
                pstmt2.setInt(1, rs.getInt("job_id"));
                rs2 = pstmt2.executeQuery();
                if(rs2.next())
                    job.setApplyNum(rs2.getInt(1));
                else
                    job.setApplyNum(0);
                job.setJobId(rs.getInt("job_id"));
                job.setJobName(rs.getString("job_name"));
                job.setJobHiringnum(rs.getInt("job_hiringnum"));
                job.setJobEndtime(rs.getTimestamp("job_endtime"));
                job.setJobState(rs.getInt("job_state"));
                Company company = new Company();
                company.setCompanyId(rs.getInt("company_id"));
                company.setCompanyName(rs.getString("company_name"));
                job.setCompany(company);
                list.add(job);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeJDBC(rs, pstmt, conn);
            DBUtil.closeJDBC(rs2, pstmt2, conn);
        }
        return list;
    }
    public List<Job> query(int companyId, String jobName) {
        List<Job> list = new ArrayList<Job>();
        Connection conn = DBUtil.getConnection();
        Statement stmt = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT tb_job.*,company_name FROM tb_job INNER JOIN tb_company on tb_job.company_id =  tb_company.company_id WHERE 1=1 ");
            if(companyId != 0 )
                sql.append("AND tb_job.company_id = " + companyId);
            if(!"".equals(jobName))
                sql.append("AND job_name like '%" + jobName +"%'");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.toString());
            while (rs.next()) {
                Job job = new Job();
                // 查询某个职位的申请人数
                String sql2 = "SELECT COUNT(*) FROM tb_jobapply WHERE job_id = ?";
                pstmt2 = conn.prepareStatement(sql2);
                pstmt2.setInt(1, rs.getInt("job_id"));
                rs2 = pstmt2.executeQuery();
                if(rs2.next())
                    job.setApplyNum(rs2.getInt(1));
                else
                    job.setApplyNum(0);
                job.setJobId(rs.getInt("job_id"));
                job.setJobName(rs.getString("job_name"));
                job.setJobHiringnum(rs.getInt("job_hiringnum"));
                job.setJobEndtime(rs.getTimestamp("job_endtime"));
                job.setJobState(rs.getInt("job_state"));
                Company company = new Company();
                company.setCompanyId(rs.getInt("company_id"));
                company.setCompanyName(rs.getString("company_name"));
                job.setCompany(company);
                list.add(job);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeJDBC(rs, stmt, conn);
            DBUtil.closeJDBC(rs2, pstmt2, conn);
        }
        return list;
    }
    public List<Job> selectJobNameByCompany(int companyID) {
        List<Job> list = new ArrayList<Job>();
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT job_id,job_name FROM tb_job WHERE company_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,companyID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Job job = new Job();
                job.setJobId(rs.getInt("job_id"));
                job.setJobName(rs.getString("job_name"));
                list.add(job);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeJDBC(rs, pstmt, conn);
        }
        return list;
    }
    /**
     * 判断职位是否有投递
     */
    public boolean hasApply(int jobId) {
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean has = false;
        try {
            String sql = "SELECT COUNT(*) FROM tb_jobapply WHERE job_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);
            rs = pstmt.executeQuery();
            if(rs.next() && rs.getInt(1) > 0) {
                has = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeJDBC(rs, pstmt, conn);
        }
        return has;
    }
    /**
     * 根据职位ID删除职位（有投递则不允许删除）
     */
    public boolean delete(int jobId) {
        if(hasApply(jobId)) return false;
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            String sql = "DELETE FROM tb_job WHERE job_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeJDBC(null, pstmt, conn);
        }
        return result;
    }
    /**
     * 修改职位信息（这里只做简单示例，实际可根据需要扩展字段）
     */
    public boolean update(Job job) {
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            String sql = "UPDATE tb_job SET job_name=?, job_hiringnum=?, job_endtime=?, job_state=?, company_id=? WHERE job_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, job.getJobName());
            pstmt.setInt(2, job.getJobHiringnum());
            pstmt.setTimestamp(3, job.getJobEndtime());
            pstmt.setInt(4, job.getJobState());
            pstmt.setInt(5, job.getCompany().getCompanyId());
            pstmt.setInt(6, job.getJobId());
            int rows = pstmt.executeUpdate();
            result = rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeJDBC(null, pstmt, conn);
        }
        return result;
    }
}