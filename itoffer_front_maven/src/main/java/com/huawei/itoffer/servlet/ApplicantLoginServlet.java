package com.huawei.itoffer.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huawei.itoffer.bean.Company;
import com.huawei.itoffer.dao.ApplicantDAO;
import com.huawei.itoffer.dao.CompanyDAO;

/**
 * Servlet implementation class ApplicantLoginServlet
 */
@WebServlet("/ApplicantLoginServlet")
public class ApplicantLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	ApplicantDAO applicantDao = new ApplicantDAO();
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
	
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String requestPath = request.getParameter("requestPath");
		int applicantID = applicantDao.login(email, password);
		if (applicantID != 0) {
			request.getSession().setAttribute("applicantID", applicantID);
			if(!"".equals(requestPath) && null!=requestPath){
                response.sendRedirect(requestPath);
                
            }else{
            	response.sendRedirect("toIndex");
            }
		
		} else {
			
			out.print("<script type='text/javascript'>");
			out.print("alert('用户名或密码错误，请重新输入！');");
			out.print("window.location.href='" + request.getContextPath() + "/login.jsp';");
			out.print("</script>");
		}
	}
}
