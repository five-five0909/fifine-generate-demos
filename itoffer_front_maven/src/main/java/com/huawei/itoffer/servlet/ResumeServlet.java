package com.huawei.itoffer.servlet;

import com.huawei.itoffer.bean.*;
import com.huawei.itoffer.dao.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.io.File;
import java.util.List;

@WebServlet("/ResumeServlet")
@MultipartConfig
public class ResumeServlet extends HttpServlet {
    private ResumeBasicInfoDAO basicInfoDAO = new ResumeBasicInfoDAO();
    private EducationDAO educationDAO = new EducationDAO();
    private ProjectDAO projectDAO = new ProjectDAO();
    private AttachmentDAO attachmentDAO = new AttachmentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer applicantId = (Integer) req.getSession().getAttribute("applicantID");
        if (applicantId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        ResumeBasicInfo basicInfo = basicInfoDAO.getByApplicantId(applicantId);
        List<Education> educationList = educationDAO.getByApplicantId(applicantId);
        List<Project> projectList = projectDAO.getByApplicantId(applicantId);
        List<Attachment> attachmentList = attachmentDAO.getByApplicantId(applicantId);
        req.setAttribute("basicInfo", basicInfo);
        req.setAttribute("educationList", educationList);
        req.setAttribute("projectList", projectList);
        req.setAttribute("attachmentList", attachmentList);
        String iframe = req.getParameter("iframe");
        if ("resume".equals(iframe)) {
            req.getRequestDispatcher("myresume.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("mycenter.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String action = req.getParameter("action");
        Integer applicantId = (Integer) req.getSession().getAttribute("applicantID");
        if (applicantId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        if ("uploadHeadShot".equals(action)) {
            // 1. 处理文件上传
            Part filePart = req.getPart("headShotFile");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            // 2. 保存到服务器目录
            String uploadDir = req.getServletContext().getRealPath("/images/profile/");
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            String savePath = uploadDir + File.separator + fileName;
            filePart.write(savePath);
            // 3. 更新数据库
            ResumeBasicInfo info = basicInfoDAO.getByApplicantId(applicantId);
            if (info == null) {
                info = new ResumeBasicInfo();
                info.setApplicantId(applicantId);
                info.setHeadShot(fileName);
                basicInfoDAO.insert(info);
            } else {
                info.setHeadShot(fileName);
                basicInfoDAO.update(info);
            }
            resp.sendRedirect("ResumeServlet?iframe=resume");
            return;
        }
        if ("saveBasicInfo".equals(action)) {
            ResumeBasicInfo info = new ResumeBasicInfo();
            info.setApplicantId(applicantId);
            info.setRealname(req.getParameter("realname"));
            info.setGender(req.getParameter("gender"));
            info.setBirthday(java.sql.Date.valueOf(req.getParameter("birthday")));
            info.setCurrentLoc(req.getParameter("currentLoc"));
            info.setResidentLoc(req.getParameter("residentLoc"));
            info.setTelephone(req.getParameter("telephone"));
            info.setEmail(req.getParameter("email"));
            info.setJobIntension(req.getParameter("jobIntension"));
            info.setJobExperience(req.getParameter("jobExperience"));
            info.setHeadShot(req.getParameter("headShot"));
            if (basicInfoDAO.getByApplicantId(applicantId) == null) {
                basicInfoDAO.insert(info);
            } else {
                basicInfoDAO.update(info);
            }
        } else if ("addEducation".equals(action)) {
            Education edu = new Education();
            edu.setApplicantId(applicantId);
            edu.setSchool(req.getParameter("school"));
            edu.setStartDate(java.sql.Date.valueOf(req.getParameter("startDate")));
            edu.setEndDate(java.sql.Date.valueOf(req.getParameter("endDate")));
            edu.setDegree(req.getParameter("degree"));
            edu.setMajor(req.getParameter("major"));
            educationDAO.insert(edu);
        } else if ("deleteEducation".equals(action)) {
            int educationId = Integer.parseInt(req.getParameter("educationId"));
            educationDAO.delete(educationId);
        } else if ("addProject".equals(action)) {
            Project p = new Project();
            p.setApplicantId(applicantId);
            p.setProjectName(req.getParameter("projectName"));
            p.setStartDate(java.sql.Date.valueOf(req.getParameter("startDate")));
            p.setEndDate(java.sql.Date.valueOf(req.getParameter("endDate")));
            p.setPosition(req.getParameter("position"));
            projectDAO.insert(p);
        } else if ("deleteProject".equals(action)) {
            int projectId = Integer.parseInt(req.getParameter("projectId"));
            projectDAO.delete(projectId);
        } else if ("addAttachment".equals(action)) {
            Attachment att = new Attachment();
            att.setApplicantId(applicantId);
            att.setFileName(req.getParameter("fileName"));
            att.setFilePath(req.getParameter("filePath"));
            attachmentDAO.insert(att);
        } else if ("deleteAttachment".equals(action)) {
            int attachmentId = Integer.parseInt(req.getParameter("attachmentId"));
            attachmentDAO.delete(attachmentId);
        }
        resp.sendRedirect("ResumeServlet?iframe=resume");
    }
} 