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

/**
 * Servlet implementation class ToIndexServlet
 */
@WebServlet("/toIndex")
public class ToIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	 CompanyDAO companyDao = new CompanyDAO();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Company> companyList=companyDao.getCompanyList();
		request.setAttribute("companyList", companyList);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	

}
