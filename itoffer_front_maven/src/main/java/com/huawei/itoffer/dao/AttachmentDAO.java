package com.huawei.itoffer.dao;

import com.huawei.itoffer.bean.Attachment;
import com.huawei.itoffer.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttachmentDAO {
    public List<Attachment> getByApplicantId(int applicantId) {
        List<Attachment> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_attachment WHERE applicant_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicantId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Attachment att = new Attachment();
                att.setAttachmentId(rs.getInt("attachment_id"));
                att.setApplicantId(rs.getInt("applicant_id"));
                att.setFileName(rs.getString("file_name"));
                att.setFilePath(rs.getString("file_path"));
                list.add(att);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Attachment att) {
        String sql = "INSERT INTO tb_attachment(applicant_id, file_name, file_path) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, att.getApplicantId());
            ps.setString(2, att.getFileName());
            ps.setString(3, att.getFilePath());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int attachmentId) {
        String sql = "DELETE FROM tb_attachment WHERE attachment_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, attachmentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
} 