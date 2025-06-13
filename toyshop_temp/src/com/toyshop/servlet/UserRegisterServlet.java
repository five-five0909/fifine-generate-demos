package com.toyshop.servlet;

import com.toyshop.bean.User;
import com.toyshop.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// 用户注册Servlet
@WebServlet("/register")
public class UserRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        userDao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // 设置请求编码
        response.setContentType("text/html;charset=UTF-8"); // 设置响应编码

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword"); // 获取确认密码字段

        // 基本校验
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "用户名和密码不能为空！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "两次输入的密码不一致！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 检查用户名是否已存在
        if (userDao.findByUsername(username) != null) {
            request.setAttribute("errorMessage", "用户名已存在！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 注意：实际项目中密码应该加密存储
        user.setRole(1); // 默认为普通用户

        boolean success = userDao.addUser(user);

        if (success) {
            // 注册成功，重定向到登录页面，并可以带一个成功消息
            response.sendRedirect("login.jsp?registrationSuccess=true");
        } else {
            request.setAttribute("errorMessage", "注册失败，请稍后再试。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 通常注册页面是通过GET请求访问的，直接转发到JSP
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}
