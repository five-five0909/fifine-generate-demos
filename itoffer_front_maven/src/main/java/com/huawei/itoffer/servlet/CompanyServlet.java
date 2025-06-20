package com.huawei.itoffer.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huawei.itoffer.bean.Company;
import com.huawei.itoffer.bean.Job;
import com.huawei.itoffer.dao.CompanyDAO;
import com.huawei.itoffer.dao.JobDAO;

/**
 * Servlet implementation class CompanyServlet
 */
@WebServlet("/CompanyServlet")
public class CompanyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取对企业信息处理的请求类型
        String type = request.getParameter("type");
        if ("select".equals(type)) {
            // 获取请求查询的企业编号
            String companyID = request.getParameter("id");
            // 根据企业编号查询企业详细信息
            CompanyDAO dao = new CompanyDAO();
            Company company = dao.getCompanyByID(companyID);
            // 将查询到的企业信息存入request请求域
            request.setAttribute("company", company);
            // 根据企业编号查询企业的所有招聘职位
            JobDAO jobdao = new JobDAO();
            List<Job> jobList = jobdao.getJobListByCompanyID(companyID);
            // 将查询到的职位列表存入request请求域
            request.setAttribute("joblist", jobList);
            // 请求转发
            request.getRequestDispatcher("recruit/company.jsp")
                        .forward(request,response);
        }
	}

	

}
