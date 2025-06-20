package com.qst.itoffer.servlet;

import com.qst.itoffer.bean.Company;
import com.qst.itoffer.dao.CompanyDAO;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * 企业信息管理Servlet
 */
@WebServlet("/CompanyServlet")
@MultipartConfig
public class CompanyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CompanyServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置请求字符编码
		request.setCharacterEncoding("UTF-8");
		// 获取请求操作类型
		String type = request.getParameter("type");
		CompanyDAO dao = new CompanyDAO();
		if("list".equals(type)){ //企业列表查询
			List<Company> list = dao.selectAll();
			// 将查询到的企业列表数据存入请求域属性中
			request.setAttribute("list", list);
			// 请求转发到企业列表页面
			request.getRequestDispatcher("manage/companyList.jsp").forward(request, response);
		}else if("updateSelect".equals(type)){ //企业信息查询
			int companyId = Integer.parseInt(request.getParameter("companyId"));
			Company company = dao.selectById(companyId);
			request.setAttribute("company", company);
			request.getRequestDispatcher("manage/companyUpdate.jsp").forward(request, response);
		}else if("update".equals(type)){ // 企业信息更新
			// 获取企业更新信息
			int companyId = Integer.parseInt(request.getParameter("companyId"));
			String companyName = request.getParameter("companyName");
			String companyArea = request.getParameter("companyArea");
			String companySize = request.getParameter("companySize");
			String companyType = request.getParameter("companyType");
			String companyBrief = request.getParameter("companyBrief");
			int companyState = (request.getParameter("companyState") == null) ? 1 : Integer.parseInt(request.getParameter("companyState"));
			int companySort = (request.getParameter("companySort") == null) ? 0 : Integer.parseInt(request.getParameter("companySort"));
			int companyViewnum = (request.getParameter("companyViewnum") == null) ? 0 : Integer.parseInt(request.getParameter("companyViewnum"));
			String companyOldPic = request.getParameter("companyOldPic");
			Part part = request.getPart("companyPic");
			String fileName = part.getSubmittedFileName();
			// 判断用户有无更新图片
			if(!"".equals(fileName)){
				fileName = System.currentTimeMillis()
						+ fileName.substring(fileName.lastIndexOf("."));
				// 将上传的文件保存在服务器根目录下的upload/companyPic子目录下
				String filepath = getServletContext().getRealPath("/");
				filepath = filepath + "upload/companyPic";
				File f = new File(filepath);
				if (!f.exists())
					f.mkdirs();
				part.write(filepath + "/" + fileName);
			}else
				fileName = companyOldPic;
			Company company = new Company(companyId,companyName,companyArea,companySize,companyType,companyBrief,companyState,companySort,companyViewnum,fileName);
			dao.update(company);
			response.sendRedirect("CompanyServlet?type=list");
		}else if("delete".equals(type)){
			int companyId = Integer.parseInt(request.getParameter("companyId"));
			if(dao.hasJob(companyId)) {
				response.getWriter().write("hasJob");
				return;
			}
			boolean result = dao.delete(companyId);
			response.getWriter().write(result ? "success" : "fail");
			return;
		}
	}

}