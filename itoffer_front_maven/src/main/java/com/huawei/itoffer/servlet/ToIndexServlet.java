package com.huawei.itoffer.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huawei.itoffer.bean.Company;
import com.huawei.itoffer.dao.CompanyDAO;
import com.huawei.itoffer.dao.JobApplyDAO;

/**
 * Servlet implementation class ToIndexServlet
 */
@WebServlet("/toIndex")
public class ToIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	 CompanyDAO companyDao = new CompanyDAO();
	 JobApplyDAO jobApplyDao = new JobApplyDAO();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Company> companyList=companyDao.getCompanyList();
		
		// 检查用户是否登录，如果登录了，就查询其已投递的职位ID
		Integer applicantId = (Integer) request.getSession().getAttribute("applicantID");
		if (applicantId != null) {
			List<Integer> appliedJobIds = jobApplyDao.getAppliedJobIds(applicantId);
			request.setAttribute("appliedJobIds", appliedJobIds);
		}

		request.setAttribute("companyList", companyList);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	

}
