package com.huawei.itoffer.dao;

import com.huawei.itoffer.bean.Education;
import com.huawei.itoffer.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EducationDAO {
    public List<Education> getByApplicantId(int applicantId) {
        List<Education> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_education WHERE applicant_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicantId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Education edu = new Education();
                edu.setEducationId(rs.getInt("education_id"));
                edu.setApplicantId(rs.getInt("applicant_id"));
                edu.setSchool(rs.getString("school"));
                edu.setStartDate(rs.getTimestamp("start_date"));
                edu.setEndDate(rs.getTimestamp("end_date"));
                edu.setDegree(rs.getString("degree"));
                edu.setMajor(rs.getString("major"));
                list.add(edu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Education edu) {
        String sql = "INSERT INTO tb_education(applicant_id, school, start_date, end_date, degree, major) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, edu.getApplicantId());
            ps.setString(2, edu.getSchool());
            ps.setTimestamp(3, new java.sql.Timestamp(edu.getStartDate().getTime()));
            ps.setTimestamp(4, new java.sql.Timestamp(edu.getEndDate().getTime()));
            ps.setString(5, edu.getDegree());
            ps.setString(6, edu.getMajor());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int educationId) {
        String sql = "DELETE FROM tb_education WHERE education_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, educationId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
} 