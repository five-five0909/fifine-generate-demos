package com.huawei.itoffer.dao;

import com.huawei.itoffer.bean.ResumeBasicInfo;
import com.huawei.itoffer.util.DBUtil;
import java.sql.*;

public class ResumeBasicInfoDAO {
    public ResumeBasicInfo getByApplicantId(int applicantId) {
        ResumeBasicInfo info = null;
        String sql = "SELECT * FROM tb_resume_basicinfo WHERE applicant_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicantId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                info = new ResumeBasicInfo();
                info.setBasicinfoId(rs.getInt("basicinfo_id"));
                info.setApplicantId(rs.getInt("applicant_id"));
                info.setRealname(rs.getString("realname"));
                info.setGender(rs.getString("gender"));
                info.setBirthday(rs.getTimestamp("birthday"));
                info.setCurrentLoc(rs.getString("current_loc"));
                info.setResidentLoc(rs.getString("resident_loc"));
                info.setTelephone(rs.getString("telephone"));
                info.setEmail(rs.getString("email"));
                info.setJobIntension(rs.getString("job_intension"));
                info.setJobExperience(rs.getString("job_experience"));
                info.setHeadShot(rs.getString("head_shot"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public boolean update(ResumeBasicInfo info) {
        String sql = "UPDATE tb_resume_basicinfo SET realname=?, gender=?, birthday=?, current_loc=?, resident_loc=?, telephone=?, email=?, job_intension=?, job_experience=?, head_shot=? WHERE applicant_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, info.getRealname());
            ps.setString(2, info.getGender());
            ps.setTimestamp(3, new java.sql.Timestamp(info.getBirthday().getTime()));
            ps.setString(4, info.getCurrentLoc());
            ps.setString(5, info.getResidentLoc());
            ps.setString(6, info.getTelephone());
            ps.setString(7, info.getEmail());
            ps.setString(8, info.getJobIntension());
            ps.setString(9, info.getJobExperience());
            ps.setString(10, info.getHeadShot());
            ps.setInt(11, info.getApplicantId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insert(ResumeBasicInfo info) {
        String sql = "INSERT INTO tb_resume_basicinfo(applicant_id, realname, gender, birthday, current_loc, resident_loc, telephone, email, job_intension, job_experience, head_shot) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, info.getApplicantId());
            ps.setString(2, info.getRealname());
            ps.setString(3, info.getGender());
            ps.setTimestamp(4, new java.sql.Timestamp(info.getBirthday().getTime()));
            ps.setString(5, info.getCurrentLoc());
            ps.setString(6, info.getResidentLoc());
            ps.setString(7, info.getTelephone());
            ps.setString(8, info.getEmail());
            ps.setString(9, info.getJobIntension());
            ps.setString(10, info.getJobExperience());
            ps.setString(11, info.getHeadShot());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
} 