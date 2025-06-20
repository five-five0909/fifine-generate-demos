package com.qst.itoffer.servlet;

import com.qst.itoffer.dao.CompanyDAO;
import com.qst.itoffer.dao.JobDAO;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qst.itoffer.bean.Company;
import com.qst.itoffer.bean.Job;
/**
 * 职位信息处理Servlet
 */
@WebServlet("/JobServlet")
public class JobServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // 获取请求操作类型
        String type = request.getParameter("type");
        // 创建职位和企业DAO对象
        JobDAO jobdao = new JobDAO();
        CompanyDAO companydao = new CompanyDAO();
        // 职位信息查询页面所需的职位信息和企业信息的查询
        if ("list".equals(type)) {
            // 获得所有职位信息
            List<Job> joblist = jobdao.selectAll();
            // 获得所有企业名称和标识信息
            List<Company> companylist = companydao.selectAllCompanyName();
            request.setAttribute("joblist", joblist);
            request.setAttribute("companylist", companylist);
            request.getRequestDispatcher("manage/jobList.jsp")
                    .forward(request,response);
            return;
        }else if("query".equals(type)){
            int companyId = Integer.parseInt(request.getParameter("companyId"));
            String jobName = request.getParameter("jobName");
            List<Job> joblist = jobdao.query(companyId,jobName);
            List<Company> companylist = companydao.selectAllCompanyName();
            request.setAttribute("joblist", joblist);
            request.setAttribute("companylist", companylist);
            request.getRequestDispatcher("manage/jobList.jsp").forward(request,
                    response);
            return;
        }else if("delete".equals(type)){
            // 删除职位
            int jobId = Integer.parseInt(request.getParameter("jobId"));
            if(jobdao.hasApply(jobId)) {
                response.getWriter().write("hasApply");
                return;
            }
            boolean result = jobdao.delete(jobId);
            response.getWriter().write(result ? "success" : "fail");
            return;
        }else if("update".equals(type)){
            // 修改职位（只允许招聘数、结束日期、招聘状态、companyId）
            int jobId = Integer.parseInt(request.getParameter("jobId"));
            String hiringnumStr = request.getParameter("jobHiringnum");
            String enddateStr = request.getParameter("jobEndtime");
            String stateStr = request.getParameter("jobState");
            String companyIdStr = request.getParameter("companyId");
            if(hiringnumStr==null || enddateStr==null || stateStr==null || companyIdStr==null
                || hiringnumStr.isEmpty() || enddateStr.isEmpty() || stateStr.isEmpty() || companyIdStr.isEmpty()) {
                response.getWriter().write("fail");
                return;
            }
            int jobHiringnum = Integer.parseInt(hiringnumStr);
            java.sql.Timestamp jobEnddate = java.sql.Timestamp.valueOf(enddateStr);
            int jobState = Integer.parseInt(stateStr);
            int companyId = Integer.parseInt(companyIdStr);
            Job job = new Job();
            job.setJobId(jobId);
            job.setJobHiringnum(jobHiringnum);
            job.setJobEndtime(jobEnddate);
            job.setJobState(jobState);
            Company company = new Company();
            company.setCompanyId(companyId);
            job.setCompany(company);
            boolean result = jobdao.update(job);
            response.getWriter().write(result ? "success" : "fail");
            return;
        }
    }
}