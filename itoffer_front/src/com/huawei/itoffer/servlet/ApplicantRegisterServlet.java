package com.huawei.itoffer.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huawei.itoffer.dao.ApplicantDAO;

@WebServlet("/ApplicantRegisterServlet")
public class ApplicantRegisterServlet extends HttpServlet{
	 ApplicantDAO dao = new ApplicantDAO();
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
   
        boolean flag = dao.isExistEmail(email);
        if(flag){
        
            out.print("<script type='text/javascript'>");
            out.print("alert('邮箱已被注册，请重新输入！');");
            out.print("window.location='register.jsp';");
            out.print("</script>");
        }else{
            
            dao.save(email,password);
           
            out.print("<script>alert('注册成功');window.location.href='login.jsp';</script>");
        }

		
	}


	
}
