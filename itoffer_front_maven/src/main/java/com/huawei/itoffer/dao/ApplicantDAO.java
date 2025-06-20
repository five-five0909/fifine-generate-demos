package com.huawei.itoffer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.huawei.itoffer.util.DBUtil;

public class ApplicantDAO {
	Connection conn = DBUtil.getConnection();
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	public boolean isExistEmail(String email) {
		boolean result = false;
		String sql = "SELECT * FROM tb_applicant WHERE applicant_email=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void save(String email, String password) {
		String sql = "INSERT INTO tb_applicant(applicant_email,applicant_pwd,applicant_registdate) VALUES(?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			pstmt.setTimestamp(3, new Timestamp(new Date().getTime()));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int login(String email, String password) {
		int applicantID = 0;
		String sql = "SELECT applicant_id FROM tb_applicant WHERE applicant_email=? and applicant_pwd=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			if (rs.next()){
				applicantID = rs.getInt("applicant_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return applicantID;
	}

}
