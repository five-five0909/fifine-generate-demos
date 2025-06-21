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
		PrintWriter out = response.getWriter();

		// 检查用户是否登录
		Integer applicantId = (Integer) request.getSession().getAttribute("applicantID");
		if (applicantId == null) {
			// 如果未登录，则重定向到登录页面
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}

		String type = request.getParameter("type");
		String iframe = request.getParameter("iframe");

		if ("apply".equals(type)) {
			String jobIdStr = request.getParameter("jobId");
			if (jobIdStr == null || jobIdStr.trim().isEmpty()) {
				// 无效的jobId
				out.print("<script>alert('职位ID无效!'); window.history.back();</script>");
				return;
			}
			int jobId = Integer.parseInt(jobIdStr);
			JobApplyDAO dao = new JobApplyDAO();

			// 检查是否已申请
			if (dao.hasApplied(applicantId, jobId)) {
				// 已申请，弹窗提示
				out.print("<script>alert('您已投递过该职位，请勿重复投递!'); window.history.back();</script>");
			} else {
				// 未申请，保存申请记录
				dao.save(jobIdStr, applicantId);
				// 跳转到个人中心的"我的申请"标签页
				response.sendRedirect(request.getContextPath() + "/mycenter.jsp?tab=apply");
			}
			return;
        } else if ("myapply".equals(type) || "apply".equals(iframe)) {
            JobApplyDAO dao = new JobApplyDAO();
            List<JobApply> jobList = dao.getJobApplyList(applicantId);
            request.setAttribute("jobList", jobList);
            request.getRequestDispatcher("/applicant/jobApply.jsp").forward(request, response);
            return;
        }
	}

}
