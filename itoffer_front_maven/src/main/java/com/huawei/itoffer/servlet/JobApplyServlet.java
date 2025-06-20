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
		if ("apply".equals(type)) {
            // 获取职位编号
            String jobid = request.getParameter("jobId");
            // 获取登录用户
            int applicantID =(Integer)request.getSession()
                    .getAttribute("applicantID");
            // 添加此用户对此职位的申请
            JobApplyDAO dao = new JobApplyDAO();
            dao.save(jobid, applicantID);
            PrintWriter out = response.getWriter();
            out.print("<script>alert('申请成功');window.location.href='JobApplyServlet?type=myapply';</script>");
        } else if ("myapply".equals(type)) {
            // 获取登录用户
        	int applicantID = (Integer) request.getSession()
                    .getAttribute("applicantID");
            // 根据用户标识查询此用户申请的所有职位
            JobApplyDAO dao = new JobApplyDAO();
            List<JobApply> jobList = dao.getJobApplyList(applicantID);
            request.setAttribute("jobList", jobList);
            request.getRequestDispatcher("applicant/jobApply.jsp").forward(
                    request, response);
        }
	}

}
