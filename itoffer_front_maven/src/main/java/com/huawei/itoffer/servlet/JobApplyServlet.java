package com.huawei.itoffer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huawei.itoffer.bean.Applicant;
import com.huawei.itoffer.bean.JobApply;
import com.huawei.itoffer.dao.JobApplyDAO;

/**
 * Servlet implementation class JobApplyServlet
 */
@WebServlet("/JobApplyServlet")
public class JobApplyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/html; charset=UTF-8");
		String type = request.getParameter("type");
		String iframe = request.getParameter("iframe");
		if ("apply".equals(type)) {
            // 保存申请
            String jobid = request.getParameter("jobId");
            int applicantID = (Integer) request.getSession().getAttribute("applicantID");
            JobApplyDAO dao = new JobApplyDAO();
            dao.save(jobid, applicantID);
            // 保存后直接查并回显
            List<JobApply> jobList = dao.getJobApplyList(applicantID);
            request.setAttribute("jobList", jobList);
            request.getRequestDispatcher("applicant/jobApply.jsp").forward(request, response);
            return;
        } else if ("myapply".equals(type) || "apply".equals(iframe)) {
            int applicantID = (Integer) request.getSession().getAttribute("applicantID");
            JobApplyDAO dao = new JobApplyDAO();
            List<JobApply> jobList = dao.getJobApplyList(applicantID);
            request.setAttribute("jobList", jobList);
            request.getRequestDispatcher("applicant/jobApply.jsp").forward(request, response);
            return;
        }
	}

}
