package com.toyshop.servlet;

import com.toyshop.bean.User;
import com.toyshop.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// 用户登录Servlet
@WebServlet("/login")
public class UserLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        userDao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "用户名和密码不能为空！");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        User user = userDao.findByUsername(username);

        // 实际项目中，密码验证应该是比较哈希值而不是明文比较
        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user); // 将用户对象存入Session

            // 根据角色重定向
            if (user.getRole() == 2) { // 管理员
                response.sendRedirect(request.getContextPath() + "/admin/toy_manage.jsp"); // 直接到管理页，或者通过Servlet
            } else { // 普通用户
                response.sendRedirect(request.getContextPath() + "/index"); // 重定向到首页Servlet
            }
        } else {
            request.setAttribute("errorMessage", "用户名或密码错误！");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GET请求直接显示登录页面
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
