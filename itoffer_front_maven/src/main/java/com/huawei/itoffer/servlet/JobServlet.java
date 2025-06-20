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
import com.huawei.itoffer.bean.Job;
import com.huawei.itoffer.bean.JobApply;
import com.huawei.itoffer.dao.JobApplyDAO;
import com.huawei.itoffer.dao.JobDAO;

/**
 * Servlet implementation class JobServlet
 */
@WebServlet("/JobServlet")
public class JobServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		String type = request.getParameter("type");
		JobDAO dao = new JobDAO();
		// 职位详情查询
		if ("select".equals(type)) {
			// 获取职位编号
			String jobid = request.getParameter("jobid");
			// 根据职位编号查询职位详细信息
			Job job = dao.getJobByID(jobid);
			// 将职位信息对象存入request对象
			request.setAttribute("job", job);
			// 将企业信息对象存入request对象
			request.setAttribute("company", job.getCompany());
			request.getRequestDispatcher("recruit/job.jsp").forward(request, response);
		}
		if ("delete".equals(type)) {
			String applyId = request.getParameter("applyId");
			boolean result = dao.deleteJob(applyId);
			if (result) {
				PrintWriter out = response.getWriter();
				out.print("<script>alert('删除成功');window.location.href='JobApplyServlet?type=myapply';</script>");
			} else {
				PrintWriter out = response.getWriter();
				out.print("<script>alert('删除失败');window.location.href='JobApplyServlet?type=myapply';</script>");
			}
		}

	}

}
