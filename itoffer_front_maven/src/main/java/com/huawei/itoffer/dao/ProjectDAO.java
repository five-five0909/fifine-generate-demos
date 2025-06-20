package com.huawei.itoffer.dao;

import com.huawei.itoffer.bean.Project;
import com.huawei.itoffer.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {
    public List<Project> getByApplicantId(int applicantId) {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_project WHERE applicant_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicantId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Project p = new Project();
                p.setProjectId(rs.getInt("project_id"));
                p.setApplicantId(rs.getInt("applicant_id"));
                p.setProjectName(rs.getString("project_name"));
                p.setStartDate(rs.getTimestamp("start_date"));
                p.setEndDate(rs.getTimestamp("end_date"));
                p.setPosition(rs.getString("position"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Project p) {
        String sql = "INSERT INTO tb_project(applicant_id, project_name, start_date, end_date, position) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getApplicantId());
            ps.setString(2, p.getProjectName());
            ps.setTimestamp(3, new java.sql.Timestamp(p.getStartDate().getTime()));
            ps.setTimestamp(4, new java.sql.Timestamp(p.getEndDate().getTime()));
            ps.setString(5, p.getPosition());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int projectId) {
        String sql = "DELETE FROM tb_project WHERE project_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
} 